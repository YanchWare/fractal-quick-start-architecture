package com.yanchware.fractalcloud.samples.quickstart.architecture.deploy.livesystems;

import com.yanchware.fractal.sdk.domain.blueprint.FractalIdValue;
import com.yanchware.fractal.sdk.domain.livesystem.LiveSystemComponent;
import com.yanchware.fractal.sdk.domain.livesystem.LiveSystemIdValue;

import java.util.Collection;

public interface FractalSystem {
  LiveSystemIdValue liveSystemId();

  FractalIdValue fractalId();

  String description();

  Collection<? extends LiveSystemComponent> components();
}
