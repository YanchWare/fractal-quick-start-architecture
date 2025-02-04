package com.yanchware.fractalcloud.samples.quickstart.architecture;

import com.yanchware.fractal.sdk.Automaton;
import com.yanchware.fractal.sdk.domain.environment.EnvironmentIdValue;
import com.yanchware.fractal.sdk.domain.environment.EnvironmentType;
import com.yanchware.fractal.sdk.domain.environment.ManagementEnvironment;
import com.yanchware.fractal.sdk.domain.environment.OperationalEnvironment;
import com.yanchware.fractal.sdk.domain.exceptions.InstantiatorException;

import java.util.UUID;

import static com.yanchware.fractalcloud.samples.quickstart.architecture.Constants.*;

public class Environments {

  private final ManagementEnvironment.ManagementEnvironmentBuilder nonProduction;
  private final ManagementEnvironment.ManagementEnvironmentBuilder production;
  private final Automaton automaton;

  public Environments() {
    production = getEnvironment(
      "Fractal Bank Production",
      PRODUCTION_MANAGEMENT_ENVIRONMENT_SHORT_NAME,
      FRACTAL_PRODUCTION_MANAGEMENT_RESOURCE_GROUP_ID,
      AZURE_PRODUCTION_MANAGEMENT_SUBSCRIPTION_ID);
    nonProduction = getEnvironment(
      "Fractal Bank Non Production",
      NON_PRODUCTION_ENVIRONMENT_SHORT_NAME,
      FRACTAL_NON_PRODUCTION_MANAGEMENT_RESOURCE_GROUP_ID,
      AZURE_NON_PRODUCTION_MANAGEMENT_SUBSCRIPTION_ID);
    try {
      automaton = Automaton.getInstance();
    } catch (InstantiatorException e) {
      throw new RuntimeException(e);
    }
  }

  public QuickStartEnvironment productionDomainX() {
    return new QuickStartEnvironment(
      automaton,
      automaton.getEnvironmentBuilder()
        .withManagementEnvironment(production.withOperationalEnvironment(
            OperationalEnvironment.builder()
              .withName("Production - Domain X")
              .withResourceGroup(FRACTAL_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID)
              .withShortName(PRODUCTION_OPERATIONAL_ENVIRONMENT_SHORT_NAME)
              .withAzureSubscription(AZURE_REGION, AZURE_PRODUCTION_OPERATIONAL_SUBSCRIPTION_ID)
              .build())
          .build())
        .build());
  }

  public QuickStartEnvironment nonProductionDomainX() {
    return new QuickStartEnvironment(
      automaton,
      automaton.getEnvironmentBuilder()
        .withManagementEnvironment(nonProduction.withOperationalEnvironment(
            OperationalEnvironment.builder()
              .withName("Staging - Domain X")
              .withResourceGroup(FRACTAL_NON_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID)
              .withShortName(NON_PRODUCTION_OPERATIONAL_ENVIRONMENT_SHORT_NAME)
              .withAzureSubscription(AZURE_REGION, AZURE_NON_PRODUCTION_OPERATIONAL_SUBSCRIPTION_ID)
              .build())
          .build())
        .build());
  }

  private ManagementEnvironment.ManagementEnvironmentBuilder getEnvironment(
    String displayName,
    String shortName,
    UUID resourceGroupId,
    UUID subscriptionId)
  {
    return ManagementEnvironment.builder()
      .withId(new EnvironmentIdValue(
        EnvironmentType.PERSONAL,
        FRACTAL_OWNER_ID,
        shortName
      ))
      .withName(displayName)
      .withResourceGroup(resourceGroupId)
      .withAzureCloudAgent(
        AZURE_REGION,
        AZURE_TENANT_ID,
        subscriptionId);
  }
}
