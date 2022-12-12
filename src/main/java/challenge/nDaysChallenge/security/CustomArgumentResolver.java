//package challenge.nDaysChallenge.security;
//
//import challenge.nDaysChallenge.domain.Member;
//import challenge.nDaysChallenge.domain.MemberAdapter;
//import challenge.nDaysChallenge.dto.request.LoginRequestDto;
//import challenge.nDaysChallenge.dto.request.MemberRequestDto;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.MethodParameter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//import java.security.Principal;
//
//@Slf4j
//public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        Class<?> parameterType = parameter.getParameterType();
//        return MemberAdapter.class.equals(parameterType);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter,
//                                  ModelAndViewContainer mavContainer,
//                                  NativeWebRequest webRequest,
//                                  WebDataBinderFactory binderFactory) throws Exception {
//
//        MemberAdapter memberAdapter = null;
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if(authentication != null ) {
//            Object principal = authentication.getPrincipal();
//
//            if (principal==null){ //NPE 오류 발생
//                log.info("principal 객체가 없습니다.");
//            }
//
//            if (principal instanceof MemberAdapter){
//                memberAdapter = (MemberAdapter) principal;
//            }
//
//            return memberAdapter;
//        } else {
//            log.info("CustomArgumentResolver - MemberAdapter 객체가 null입니다.");
//            return null;
//        }
//
//    }
//}
