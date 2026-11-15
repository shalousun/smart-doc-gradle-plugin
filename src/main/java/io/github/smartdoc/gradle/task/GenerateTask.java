/*
 * smart-doc
 *
 * Copyright (C) 2018-2026 smart-doc
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.smartdoc.gradle.task;

import io.github.smartdoc.generation.DocGenerator;
import io.github.smartdoc.gradle.constant.GlobalConstants;
import io.github.smartdoc.gradle.extension.SmartDocPluginExtension;
import io.github.smartdoc.model.ApiConfig;
import com.thoughtworks.qdox.JavaProjectBuilder;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

/**
 * Generate several document formats from one parse.
 * <p>
 * Formats are read from {@code -Psmartdoc.formats} (or {@code -Pformats}) and fall back
 * to the {@code formats} property of the {@code smartdoc} extension.
 *
 * @author smart-doc
 */
public class GenerateTask extends DocBaseTask {

    @Override
    public void executeAction(ApiConfig apiConfig, JavaProjectBuilder javaProjectBuilder, Logger logger) {
        String formats = this.resolveFormats();
        executeSafely(apiConfig, () -> DocGenerator.generate(apiConfig, javaProjectBuilder, formats), logger);
    }

    private String resolveFormats() {
        Project project = getProject();
        Object property = project.findProperty(GlobalConstants.SMARTDOC_FORMATS);
        if (property == null) {
            property = project.findProperty(GlobalConstants.FORMATS);
        }
        if (property != null) {
            return property.toString();
        }
        SmartDocPluginExtension extension = project.getExtensions().getByType(SmartDocPluginExtension.class);
        return extension.getFormats();
    }

}
