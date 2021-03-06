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
package org.flowable.engine.impl.history.async.json.transformer;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.history.async.HistoryJsonConstants;
import org.flowable.engine.impl.persistence.entity.HistoricDetailEntityManager;
import org.flowable.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.flowable.engine.impl.persistence.entity.HistoryJobEntity;
import org.flowable.engine.impl.persistence.entity.data.HistoricDetailDataManager;
import org.flowable.engine.impl.util.CommandContextUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class FormPropertiesSubmittedHistoryJsonTransformer extends AbstractHistoryJsonTransformer {

    @Override
    public String getType() {
        return HistoryJsonConstants.TYPE_FORM_PROPERTIES_SUBMITTED;
    }

    @Override
    public boolean isApplicable(ObjectNode historicalData, CommandContext commandContext) {
        String activityId = getStringFromJson(historicalData, HistoryJsonConstants.ACTIVITY_ID);
        if (StringUtils.isNotEmpty(activityId)) {
            HistoricActivityInstance activityInstance = findHistoricActivityInstance(commandContext, 
                    getStringFromJson(historicalData, HistoryJsonConstants.EXECUTION_ID), activityId);
            
            if (activityInstance == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void transformJson(HistoryJobEntity job, ObjectNode historicalData, CommandContext commandContext) {
        HistoricDetailDataManager historicDetailDataManager = CommandContextUtil.getProcessEngineConfiguration(commandContext).getHistoricDetailDataManager();
        
        boolean hasMoreFormProperties = true;
        int counter = 1;
        while (hasMoreFormProperties) {
            
            String propertyId = getStringFromJson(historicalData, HistoryJsonConstants.FORM_PROPERTY_ID + counter);
            if (StringUtils.isEmpty(propertyId)) {
                break;
            }
            
            HistoricFormPropertyEntity historicFormPropertyEntity = historicDetailDataManager.createHistoricFormProperty();
            historicFormPropertyEntity.setProcessInstanceId(getStringFromJson(historicalData, HistoryJsonConstants.PROCESS_INSTANCE_ID));
            historicFormPropertyEntity.setExecutionId(getStringFromJson(historicalData, HistoryJsonConstants.EXECUTION_ID));
            historicFormPropertyEntity.setTaskId(getStringFromJson(historicalData, HistoryJsonConstants.TASK_ID));
            historicFormPropertyEntity.setPropertyId(propertyId);
            historicFormPropertyEntity.setPropertyValue(getStringFromJson(historicalData, HistoryJsonConstants.FORM_PROPERTY_VALUE + counter));
            historicFormPropertyEntity.setTime(getDateFromJson(historicalData, HistoryJsonConstants.CREATE_TIME));
    
            String activityId = getStringFromJson(historicalData, HistoryJsonConstants.ACTIVITY_ID);
            if (StringUtils.isNotEmpty(activityId)) {
                HistoricActivityInstance activityInstance = findHistoricActivityInstance(commandContext, 
                        getStringFromJson(historicalData, HistoryJsonConstants.EXECUTION_ID), activityId);
                
                historicFormPropertyEntity.setActivityInstanceId(activityInstance.getId());
            }
    
            HistoricDetailEntityManager historicDetailEntityManager = CommandContextUtil.getProcessEngineConfiguration(commandContext).getHistoricDetailEntityManager();
            historicDetailEntityManager.insert(historicFormPropertyEntity);
            
            counter++;
        }
    }

}
