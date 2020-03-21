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

package com.sky.xposed.core.component;

import com.sky.xposed.core.base.AbstractComponent;
import com.sky.xposed.core.interfaces.XCoreManager;
import com.sky.xposed.core.interfaces.XEvent;
import com.sky.xposed.core.interfaces.XEventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sky on 2020-03-17.
 */
public class EventManager extends AbstractComponent implements XEventManager {

    private Map<Class<? extends XEvent>, List<XEvent>> mClassListMap = new HashMap<>();

    private XCoreManager mCoreManager;

    private EventManager(Build build) {
        mCoreManager = build.mCoreManager;
    }

    @Override
    public <T extends XEvent> void register(Class<T> eClass, T observer) {

        List<XEvent> events = mClassListMap.get(eClass);

        if (events == null) {
            events = new ArrayList<>();
            mClassListMap.put(eClass, events);
        }

        events.add(observer);
    }

    @Override
    public <T extends XEvent> void unregister(Class<T> eClass, T observer) {

        List<XEvent> events = mClassListMap.get(eClass);

        if (events != null) events.remove(observer);
    }

    @Override
    public <T extends XEvent> void notice(Class<T> eClass, Callback<T> callback) {

        List<XEvent> events = mClassListMap.get(eClass);

        if (events == null) return;

        for (XEvent event : events) {
            callback.onHandler(eClass.cast(event));
        }
    }

    public static class Build {

        private XCoreManager mCoreManager;

        public Build(XCoreManager coreManager) {
            mCoreManager = coreManager;
        }

        public XEventManager build() {
            return new EventManager(this);
        }
    }
}
