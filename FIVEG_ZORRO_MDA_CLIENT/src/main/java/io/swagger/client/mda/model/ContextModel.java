
package io.swagger.client.mda.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
/**
 * ContextModel
 */

//manually generated
public class ContextModel {
    @SerializedName("resource_id")
    private String resourceId = null;

    @SerializedName("slice_id")
    private String sliceId = null;

    @SerializedName("parent_id")
    private String parentId = null;


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getSliceId() {
        return sliceId;
    }

    public void setSliceId(String sliceId) {
        this.sliceId = sliceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
