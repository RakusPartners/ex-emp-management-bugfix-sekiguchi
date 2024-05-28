package com.example.controller;

import java.lang.reflect.Field;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Administrator;
import com.example.form.InsertAdministratorForm;
import com.example.form.LoginForm;
import com.example.service.AdministratorService;

import jakarta.servlet.http.HttpSession;

/**
 * 管理者情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController {

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private HttpSession session;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public InsertAdministratorForm setUpInsertAdministratorForm() {
		return new InsertAdministratorForm();
	}

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：管理者を登録する
	/////////////////////////////////////////////////////
	/**
	 * 管理者登録画面を出力します.
	 * 
	 * @return 管理者登録画面
	 */
	@GetMapping("/toInsert")
	public String toInsert(Model model) {
		return "administrator/insert";
	}

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン画面へリダイレクト
	 */
	@PostMapping("/insert")
	public String insert(@Validated InsertAdministratorForm form,BindingResult result,Model model) {
		if(result.hasErrors()){
			return toInsert(model);
		}

		//エラー文を持たせ、スコープに格納→　それをHTMLでエラー文として表示させる
		//フォームで取得した２つの値に対して、if文で処理を分岐
		if(form.getPassword().equals(form.getPassword2())){
		//いままで通りの動き
			//登録済：ログイン画面に遷移
			//未登録：今まで通りinsert intoで追加
			//未登録
			if(administratorService.findByMailAddress(form.getMailAddress())== null){
				Administrator administrator = new Administrator();
				// フォームからドメインにプロパティ値をコピー
				BeanUtils.copyProperties(form, administrator);
				administratorService.insert(administrator);
				return "redirect:/";
			}else{//登録
				return "redirect:/";
			}
		}else{
			//returnで管理者登録画面まで戻るかつエラー文の記載
			//エラー文をerrorに入れる
			session.setAttribute("error", "パスワードが正しくありません");

		   //if文の条件を記載するためのオブジェクト作成→スコープ格納 →オブジェクトを変えるのは×？
			// Administrator administrator = new Administrator();
			// administrator.setPassword(form.getPassword());
			// administrator.setPassword2(form.getPassword2());
			// session.setAttribute("administrator", administrator);
			
			return "redirect:/toInsert";
			//redirectをしているのでmodelスコープだとなくなる
		}

		// String pass= form.getPassword();
		// String pass2 = form.getPassword2();
		// if(pass.equals(pass2)){
		// 	result.rejectValue("password2","パスワードが正しくありません");
		// }

		
		// エラーの手動追加
			// FieldError fieldError = new FieldError(result.getObjectName(), "password2","パスワードが正しくありません");
			// result.addError(fieldError);

		//エラー文をerrorに入れる
			// model.addAttribute("error", "パスワードが正しくありません");

		//if文の条件を記載するためのオブジェクト作成→スコープ格納 →オブジェクトを変えるのは×？
			// Administrator administrator = new Administrator();
			// administrator.setPassword(form.getPassword());
			// administrator.setPassword2(form.getPassword2());
			// model.addAttribute("administrator", administrator);

		
	}


	
	/////////////////////////////////////////////////////
	// ユースケース：ログインをする
	/////////////////////////////////////////////////////
	/**
	 * ログイン画面を出力します.
	 * 
	 * @return ログイン画面
	 */
	@GetMapping("/")
	public String toLogin() {
		return "administrator/login";
	}

	/**
	 * ログインします.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン後の従業員一覧画面
	 */
	@PostMapping("/login")
	public String login(LoginForm form, RedirectAttributes redirectAttributes) {
		Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
		if (administrator == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
			return "redirect:/";
		}
		return "redirect:/employee/showList";
	}

	/////////////////////////////////////////////////////
	// ユースケース：ログアウトをする
	/////////////////////////////////////////////////////
	/**
	 * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	 * 
	 * @return ログイン画面
	 */
	@GetMapping(value = "/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}

}
