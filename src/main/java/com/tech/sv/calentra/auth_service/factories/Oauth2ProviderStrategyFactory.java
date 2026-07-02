package com.tech.sv.calentra.auth_service.factories;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tech.sv.calentra.auth_service.enums.OAuth2Providers;
import com.tech.sv.calentra.auth_service.strategies.Oauth2.Oauth2ProvidersStrategy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Oauth2ProviderStrategyFactory {

	private final Map<OAuth2Providers, Oauth2ProvidersStrategy> factories;
	
	
	public Oauth2ProviderStrategyFactory(List<Oauth2ProvidersStrategy> StrategyList) {
		this.factories = StrategyList.stream()
				.collect(Collectors.toMap(Oauth2ProvidersStrategy::provider, Function.identity()));
	}
	
	public Oauth2ProvidersStrategy getStrategies(OAuth2Providers type) {
		Oauth2ProvidersStrategy strategy = factories.get(type);
		
		if(strategy == null) {
			throw new IllegalArgumentException("Unsupported OAuth2 provider: " + type);
		}
		
		return strategy;
	}
}
