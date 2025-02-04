package com.yanchware.fractalcloud.samples.quickstart.architecture.deploy;

import com.yanchware.fractal.sdk.domain.exceptions.InstantiatorException;
import com.yanchware.fractal.sdk.domain.livesystem.LiveSystemIdValue;
import com.yanchware.fractalcloud.samples.quickstart.architecture.Environments;
import com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.configuration.EnvVarConfiguration;
import com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.configuration.Environment;
import com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.livesystems.Containers;
import com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.livesystems.FractalSystem;

import java.util.List;

import static com.yanchware.fractalcloud.samples.quickstart.architecture.Constants.FRACTAL_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID;

public class App {

  public static void main(String[] args) throws InstantiatorException {
    var environmentsService = new Environments();
    var configuration = new EnvVarConfiguration();
    var environment = configuration.getEnvironment();
    var deleteLiveSystems = args.length == 1 && args[0].equalsIgnoreCase("delete");

    if (environment == Environment.NON_PRODUCTION) {
      if(deleteLiveSystems) {
        environmentsService.nonProductionDomainX().delete(getIds(getLiveSystems(environment)));
      } else {
        environmentsService.nonProductionDomainX().deploy(getLiveSystems(environment));
      }
    } else if (environment == Environment.PRODUCTION) {
      if(deleteLiveSystems) {
        environmentsService.productionDomainX().delete(getIds(getLiveSystems(environment)));
      } else {
        environmentsService.productionDomainX().deploy(getLiveSystems(environment));
      }
    } else {
      throw new IllegalArgumentException("Unknown environment: " + environment);
    }
  }

  private static List<LiveSystemIdValue> getIds(List<FractalSystem> liveSystems) {
    return liveSystems.stream().map(FractalSystem::liveSystemId).toList();
  }

  private static List<FractalSystem> getLiveSystems(Environment environment) {
    return List.of(
      new Containers(
        new LiveSystemIdValue(FRACTAL_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID.toString(), "containers"),
        FRACTAL_PRODUCTION_OPERATIONAL_RESOURCE_GROUP_ID,
        "Containerized workloads",
        environment));
  }
}
