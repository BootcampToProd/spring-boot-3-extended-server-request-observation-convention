package com.bootcamptoprod.serverrequestobservationconvention.config;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import org.springframework.http.server.observation.DefaultServerRequestObservationConvention;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.stereotype.Component;


/**
 * The type Bootcamp to prod extended server request observation convention.
 * Useful for adding tags in controller metrics in addition to default tags.
 */
@Component
public class BootcampToProdExtendedServerRequestObservationConvention extends DefaultServerRequestObservationConvention {

    // Calling super class constructor with custom name is optional. Use this constructor only if you want to customize the name of metrics.
    // Basically, this class by default overrides the property that is set inside the management.observations.http.server.requests.name property in application.yml file with default name. If you want to customize the metric name then uncomment the constructor and set the name as per your requirement.
    // Applications, that do not extend the DefaultServerRequestObservationConvention class can simply set the custom metric name by setting management.observations.http.server.requests.name property in application.yml file
   /* public BootcampToProdExtendedServerRequestObservationConvention(@Value("${management.observations.http.server.requests.name}") String httpServerRequestsName) {
        super(httpServerRequestsName);
    }*/

    @Override
    public KeyValues getLowCardinalityKeyValues(ServerRequestObservationContext context) {
        // here, we just want to have an additional KeyValue to the observation, keeping the default values
        return super.getLowCardinalityKeyValues(context).and(additionalTags(context));
    }

    protected KeyValues additionalTags(ServerRequestObservationContext context) {
        KeyValues keyValues = KeyValues.empty();

        // Optional tag which will be present in metrics only when the condition is evaluated to true
        if (context.getCarrier() != null && context.getCarrier().getParameter("user") != null) {
            keyValues = keyValues.and(KeyValue.of("user", context.getCarrier().getParameter("user")));
        }

        // Custom tag which will be present in all the controller metrics
        keyValues = keyValues.and(KeyValue.of("tag", "value"));

        return keyValues;
    }

}
