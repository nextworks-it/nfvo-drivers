/*
 * 5GZorro eLicense Manager Core Swagger
 * Swagger 5GZorro for the centralized eLicense Manager System of the project
 *
 * OpenAPI spec version: 0.1.0
 * Contact: guillermo.gomez.external@atos.net
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.elma.auth;

import io.swagger.client.elma.Pair;

import java.util.Map;
import java.util.List;

public interface Authentication {
    /**
     * Apply authentication settings to header and query params.
     *
     * @param queryParams List of query parameters
     * @param headerParams Map of header parameters
     */
    void applyToParams(List<Pair> queryParams, Map<String, String> headerParams);
}