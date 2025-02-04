package com.yanchware.fractalcloud.samples.quickstart.architecture;

import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.AzureRegion;

import java.util.UUID;

public class Constants {
  protected static final AzureRegion AZURE_REGION = AzureRegion.WEST_EUROPE;

  protected static final UUID FRACTAL_OWNER_ID =
    UUID.fromString("$FRACTAL_OWNER_ID");
  protected static final UUID AZURE_TENANT_ID =
    UUID.fromString("$AZURE_TENANT_ID");

  // Production
  protected static final String PRODUCTION_MANAGEMENT_ENVIRONMENT_SHORT_NAME = "quick-start-prod";
  protected static final String PRODUCTION_OPERATIONAL_ENVIRONMENT_SHORT_NAME = "quick-start-prod-domain-x";
  protected static final UUID FRACTAL_PRODUCTION_MANAGEMENT_RESOURCE_GROUP_ID =
    UUID.fromString("$FRACTAL_PRODUCTION_MANAGEMENT_RESOURCE_GROUP_ID");
  protected static final UUID AZURE_PRODUCTION_MANAGEMENT_SUBSCRIPTION_ID =
    UUID.fromString("$AZURE_PRODUCTION_MANAGEMENT_SUBSCRIPTION_ID");
  public static final UUID FRACTAL_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID =
    UUID.fromString("$FRACTAL_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID");
  protected static final UUID AZURE_PRODUCTION_OPERATIONAL_SUBSCRIPTION_ID =
    UUID.fromString("$AZURE_PRODUCTION_OPERATIONAL_SUBSCRIPTION_ID");

  // Non Production
  protected static final String NON_PRODUCTION_ENVIRONMENT_SHORT_NAME = "quick-start-non-prod";
  protected static final String NON_PRODUCTION_OPERATIONAL_ENVIRONMENT_SHORT_NAME = "quick-start-non-prod-domain-x";
  protected static final UUID FRACTAL_NON_PRODUCTION_MANAGEMENT_RESOURCE_GROUP_ID =
    UUID.fromString("$FRACTAL_NON_PRODUCTION_MANAGEMENT_RESOURCE_GROUP_ID");
  protected static final UUID AZURE_NON_PRODUCTION_MANAGEMENT_SUBSCRIPTION_ID =
    UUID.fromString("$AZURE_NON_PRODUCTION_MANAGEMENT_SUBSCRIPTION_ID");
  public static final UUID FRACTAL_NON_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID =
    UUID.fromString("$FRACTAL_NON_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID");
  protected static final UUID AZURE_NON_PRODUCTION_OPERATIONAL_SUBSCRIPTION_ID =
    UUID.fromString("$AZURE_NON_PRODUCTION_OPERATIONAL_SUBSCRIPTION_ID");
}
