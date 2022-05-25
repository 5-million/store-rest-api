package xyz.fm.storerestapi.controller;

import org.springframework.context.annotation.Import;
import xyz.fm.storerestapi.jwt.JwtAccessDeniedHandler;
import xyz.fm.storerestapi.jwt.JwtAuthenticationEntryPoint;
import xyz.fm.storerestapi.jwt.JwtProvider;

@Import(value = {JwtProvider.class, JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class})
public abstract class ControllerTestConfig {
}
