package valio.auth_service.strategies.Jwt;

public interface ValidationRuleStrategy<T> {
    void validate(T request);
}
