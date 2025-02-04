package com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.livesystems;


import com.yanchware.fractal.sdk.domain.ComponentLink;
import com.yanchware.fractal.sdk.domain.blueprint.FractalIdValue;
import com.yanchware.fractal.sdk.domain.livesystem.LiveSystemComponent;
import com.yanchware.fractal.sdk.domain.livesystem.LiveSystemIdValue;
import com.yanchware.fractal.sdk.domain.livesystem.caas.CaaSAmbassador;
import com.yanchware.fractal.sdk.domain.livesystem.caas.CaaSElasticLogging;
import com.yanchware.fractal.sdk.domain.livesystem.caas.CaaSKubernetesWorkload;
import com.yanchware.fractal.sdk.domain.livesystem.caas.CaaSPrometheus;
import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.AzurePostgreSqlDatabase;
import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.AzurePostgreSqlDbms;
import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.AzureRegion;
import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.AzureResourceGroup;
import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.aks.AzureKubernetesService;
import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.aks.AzureNodePool;
import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.cosmos.AzureCosmosPostgreSqlDatabase;
import com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.cosmos.AzureCosmosPostgreSqlDbms;
import com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.configuration.Environment;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.AzureMachineType.STANDARD_B4MS;
import static com.yanchware.fractal.sdk.domain.livesystem.paas.providers.azure.AzureMachineType.STANDARD_D11_V2;

public record Containers(
  LiveSystemIdValue liveSystemId,
  UUID resourceGroupId,
  String description,
  Environment environment
) implements FractalSystem
{
  static final AzureRegion REGION = AzureRegion.WEST_EUROPE;

  public static CaaSAmbassador getAmbassador() {
    return CaaSAmbassador.builder()
      .withId("ambassador")
      .withDisplayName("ambassador")
      .withHostOwnerEmail("email@fractal-bank.com")
      .withAcmeProviderAuthority("https://acme-v02.api.letsencrypt.org/directory")
      .withTlsSecretName("env-tls-cert")
      .withNamespace("ambassador-01")
      .build();
  }

  private static Collection<? extends AzureNodePool> getNodePools(Environment environment) {
    var baseNodepool = AzureNodePool.builder()
      .withName("linuxdynamic")
      .withMachineType(STANDARD_B4MS)
      .withAutoscalingEnabled(true)
      .withMinNodeCount(1)
      .withMaxNodeCount(10)
      .build();

    if (environment == Environment.NON_PRODUCTION) {
      return List.of(
        baseNodepool,
        AzureNodePool.builder()
          .withName("ldynamic")
          .withMachineType(STANDARD_D11_V2)
          .withAutoscalingEnabled(true)
          .withMinNodeCount(1)
          .withMaxNodeCount(10)
          .build());
    } else {
      return List.of(baseNodepool);
    }

  }

  public FractalIdValue fractalId() {
    return new FractalIdValue(
      resourceGroupId.toString(),
      "containers",
      "1.0");
  }

  public Collection<? extends LiveSystemComponent> components() {
    var resourceGroup = AzureResourceGroup.builder()
      .withName(String.format("rg-%s", liveSystemId.name()))
      .withRegion(REGION)
      .withTag("Purpose", "Samples")
      .build();

    var dbmsId = String.format("sql-%s", liveSystemId.name());
    var dbId = String.format("%s-nice-db", dbmsId);
    var db = getSqlDbms(dbmsId, dbId, resourceGroup);
    var aks = getAks(String.format("aks-%s", liveSystemId.name()), dbId, resourceGroup, environment);
    var cosmosDb = getAzureCosmosPostgreSqlDatabase(String.format("cosmos-%s", liveSystemId.name()), resourceGroup);


    return List.of(aks, db, cosmosDb);
  }

  private AzureCosmosPostgreSqlDbms getAzureCosmosPostgreSqlDatabase(String id, AzureResourceGroup resourceGroup) {
    return AzureCosmosPostgreSqlDbms.builder()
      .withId(id)
      .withDisplayName(id)
      .withAzureResourceGroup(resourceGroup)
      .withCosmosEntity(
        AzureCosmosPostgreSqlDatabase.builder()
          .withId(String.format("%s-nice-db", id))
          .withDisplayName(id)
          .build())
      .build();
  }

  private AzurePostgreSqlDbms getSqlDbms(String id, String dbId, AzureResourceGroup resourceGroup) {
    return AzurePostgreSqlDbms.builder()
      .withId(id)
      .withDisplayName(id)
      .withRegion(resourceGroup.getRegion())
      .withDatabase(
        AzurePostgreSqlDatabase.builder()
          .withId(dbId)
          .withDisplayName(dbId)
          .withName(dbId)
          .build())
      .build();
  }

  private AzureKubernetesService getAks(
    String id,
    String dbId,
    AzureResourceGroup resourceGroup,
    Environment environment)
  {
    var builder = AzureKubernetesService.builder()
      .withId(id)
      .withDisplayName(id)
      .withRegion(resourceGroup.getRegion())
      .withResourceGroup(resourceGroup)
      .withAPIGateway(getAmbassador())
      .withNodePools(getNodePools(environment))
      .withMonitoring(getMonitoringSolution())
      .withK8sWorkloadInstances(getK8sWorkloads(dbId));

    if (environment == Environment.NON_PRODUCTION) {
      builder.withLogging(getLoggingSolution());
    }

    return builder
      .build();
  }

  private CaaSElasticLogging getLoggingSolution() {
    return CaaSElasticLogging.builder()
      .withId("elastic-logging")
      .withDisplayName("elastic-logging")
      .withVersion("2.5.0")
      .withNamespace("elastic")
      .withKibana(true)
      .withAPM(true)
      .withElasticVersion("8.5.0")
      .withInstances(1)
      .withStorage("10Gi")
      .withStorageClassName("managed-csi")
      .withMemory(1)
      .withCpu(1)
      .build();
  }

  private CaaSPrometheus getMonitoringSolution() {
    return CaaSPrometheus.builder()
      .withId("prometheus")
      .withDescription("Prometheus monitoring")
      .withDisplayName("prometheus")
      .withNamespace("monitoring")
      .build();
  }

  private Collection<? extends CaaSKubernetesWorkload> getK8sWorkloads(String dbId) {
    var namespace = "app";

    var repoId = "fractal-bank-demo";
    var repositoryUri = String.format("git@github.com:YanchWare/%s.git", repoId);

    var liveSystemName = liveSystemId.name();
    var branchName = liveSystemName.toLowerCase();

    var privateSshKeySecretId = String.format("%s-fractal-deployer-ssh-private-key", liveSystemName);
    var privateSshPassphraseSecretId = String.format("%s-fractal-deployer-ssh-private-key-passphrase", liveSystemName);

    return List.of(
      CaaSKubernetesWorkload.builder()
        .withId("app-reader")
        .withDisplayName("app-reader")
        .withDescription(String.format("App Reader %s", liveSystemName))
        .withSSHRepositoryURI(repositoryUri)
        .withRepoId(repoId)
        .withBranchName(branchName)
        .withPrivateSSHKeySecretId(privateSshKeySecretId)
        .withPrivateSSHKeyPassphraseSecretId(privateSshPassphraseSecretId)
        .withNamespace(namespace)
        .withLink(ComponentLink.builder()
          .withComponentId(dbId)
          .withRoleName("reader")
          .build())
        .build(),
      CaaSKubernetesWorkload.builder()
        .withId("app-writer")
        .withDisplayName("app-writer")
        .withDescription(String.format("App Writer %s", liveSystemName))
        .withSSHRepositoryURI(repositoryUri)
        .withRepoId(repoId)
        .withBranchName(branchName)
        .withPrivateSSHKeySecretId(privateSshKeySecretId)
        .withPrivateSSHKeyPassphraseSecretId(privateSshPassphraseSecretId)
        .withNamespace(namespace)
        .withLink(ComponentLink.builder()
          .withComponentId(dbId)
          .withRoleName("admin")
          .build())
        .build()
    );
  }

}


