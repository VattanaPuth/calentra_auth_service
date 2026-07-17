package valio.auth_service.strategies.Jwt;

public interface TokenExtractorStrategy<T, R> {
    R extract(T source);
}
