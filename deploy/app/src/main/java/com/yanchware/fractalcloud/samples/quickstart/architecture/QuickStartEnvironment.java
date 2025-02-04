package com.yanchware.fractalcloud.samples.quickstart.architecture;

import com.yanchware.fractal.sdk.Automaton;
import com.yanchware.fractal.sdk.configuration.instantiation.InstantiationConfiguration;
import com.yanchware.fractal.sdk.configuration.instantiation.InstantiationWaitConfiguration;
import com.yanchware.fractal.sdk.domain.environment.EnvironmentAggregate;
import com.yanchware.fractal.sdk.domain.exceptions.InstantiatorException;
import com.yanchware.fractal.sdk.domain.livesystem.LiveSystemIdValue;
import com.yanchware.fractal.sdk.domain.livesystem.service.dtos.ProviderType;
import com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.livesystems.FractalSystem;

import java.util.List;

public class QuickStartEnvironment {
  private final Automaton automaton;
  private final EnvironmentAggregate environmentAggregate;

  public QuickStartEnvironment(Automaton automaton, EnvironmentAggregate environmentAggregate) {
    this.automaton = automaton;
    this.environmentAggregate = environmentAggregate;
  }

  public void deploy(List<FractalSystem> fractalBankSystems) throws InstantiatorException {
    var instantiationConfig =
      InstantiationConfiguration.builder().withWaitConfiguration(InstantiationWaitConfiguration.builder()
        .withWaitForInstantiation(true)
        .withTimeoutMinutes(30)
        .build()).build();

    automaton.instantiate(fractalBankSystems.stream()
      .map(x -> automaton.getLiveSystemBuilder()
        .withId(x.liveSystemId())
        .withFractalId(x.fractalId())
        .withDescription(x.description())
        .withComponents(x.components())
        .withEnvironmentId(environmentAggregate.getManagementEnvironment().getOperationalEnvironments().getFirst().getId())
        .withStandardProvider(ProviderType.AZURE).build())
      .toList(), instantiationConfig);
  }

  public void delete(List<LiveSystemIdValue> fractalBankSystemIds) throws InstantiatorException {
    automaton.delete(fractalBankSystemIds);
  }}
