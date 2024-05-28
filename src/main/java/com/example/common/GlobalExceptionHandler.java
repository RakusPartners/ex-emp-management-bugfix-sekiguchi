package com.example.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * アプリケーション内で処理されなかった例外をここでキャッチし、
 * エラーページへ遷移させます.
 * 
 * @author igamasayuki
 *
 */
//このクラスをDIコンテナに入れるため
@Component 
//HandlerExceptionResolver のインターフェースを付与
public class GlobalExceptionHandler implements HandlerExceptionResolver {

	private static final Logger LOGGER
			= LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Object obj, 
			Exception e) {
		LOGGER.error("システムエラーが発生しました！", e); //ターミナルにエラーログをはく
		return null; // ←500エラーが発生したら自動的にerror/500.htmlに遷移してくれる
		//発生したエクセプションに対して、対応するエラーのhtmlを返す。（404や500）
	}
}