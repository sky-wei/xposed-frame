/*
 * Copyright (c) 2020 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.frame.plugin;

import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.core.base.AbstractPlugin;
import com.sky.xposed.core.interfaces.XCoreManager;

/**
 * Created by sky on 2020-03-16.
 */
@APlugin()
public class DemoPlugin extends AbstractPlugin {

    public DemoPlugin(XCoreManager coreManager) {
        super(coreManager);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void hook() {

        findMethod("con.xxxx.class", "method")
                .before(param -> {


                });
    }
}
