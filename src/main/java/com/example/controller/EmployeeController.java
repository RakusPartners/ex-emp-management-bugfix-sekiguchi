package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Employee;
import com.example.form.UpdateEmployeeForm;
import com.example.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private HttpSession session;
	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@GetMapping("/showList")
	public String showList() {
		List<Employee> employeeList = employeeService.showList();
		session.setAttribute("employeeList", employeeList);
		return "employee/list";
	}

	/**
	 * 従業員情報を曖昧検索する
	 * ・空文字検索→全件検索結果表示
	 * ・指定した文字列が存在しない→「１件もありませんでした」のメッセージ＋全件検索結果表示
	 */
	@GetMapping("/searchList")
	public String showList(String name) {
			//idで全件検索
		if(name.equals(" ")){
			return "redirect:/employee/showList";
		}else if(name.equals(null)){
			session.setAttribute("name", null);
			session.setAttribute("messe","１件もありませんでした");
			return "redirect:/employee/showList";
		}else{
			List<Employee> searchList = employeeService.findByKeyname(name);
			session.setAttribute("employeeList", searchList);
			return "employee/list";
		}
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */


	@GetMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@PostMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	/**
	 * 従業員情報を曖昧検索する
	 * ・空文字検索→全件検索結果表示
	 * ・指定した文字列が存在しない→「１件もありませんでした」のメッセージ＋全件検索結果表示
	 * 
	 * 1：	all→/showList
	 * 2:   findByKeyname →/findkey作成
	 * 3:　　/member　でif文でreturnを使い分ける
	 * @return 従業員一覧画面へリダイレクト
	 */


	// @GetMapping("/member")
	// public String member(String name,Model model){
	
	// 	if(name.equals(" ")){
	// 		return "redirect:/employee/showList";
	// 	}else if(name.equals(null)){
	// 		session.setAttribute("messe","１件もありませんでした");
	// 		return "redirect:/employee/showList";
	// 	}else{
	// 		return "redirect:/employee/showList";
	// 	}
	// }
	
}
