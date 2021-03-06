/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.idm.engine.impl.persistence.entity;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.common.impl.persistence.entity.AbstractEntity;

public class PrivilegeEntityImpl extends AbstractEntity implements PrivilegeEntity {

    protected String name;

    @Override
    public Object getPersistentState() {
        Map<String, String> state = new HashMap<>();
        state.put("id", id);
        state.put("name", name);
        return state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
