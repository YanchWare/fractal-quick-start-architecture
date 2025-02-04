package com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.configuration;

public class EnvVarConfiguration implements Configuration {
  @Override
  public Environment getEnvironment() {
    var environmentStr = getVariableValue("ENVIRONMENT", true);
    return Environment.valueOf(environmentStr);
  }

  public String getVariableValue(String key, boolean throwOnMissing) {
    var value = System.getenv(key);

    if ((value == null || value.isBlank()) && throwOnMissing) {
      throw new IllegalArgumentException(
        String.format("The environment variable or system property ['%s'] is required but it has not been defined",
          key));
    }

    return value;
  }
}
