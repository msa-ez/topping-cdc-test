forEach: Aggregate
fileName: {{nameCamelCase}}Get.groovy
except: {{#attached "Event" this}}{{#outgoingRelations}}{{#checkOutgoing target}}{{/checkOutgoing}}{{/outgoingRelations}}{{/attached}}
---
package contracts.rest

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method '{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#if aggregate}}{{controllerInfo.method}}{{/if}}{{/target}}{{/outgoingRelations}}{{/attached}}'
        url ('/{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#if aggregate}}{{aggregate.namePlural}}/1{{#checkExtendVerbType controller.method controllerInfo.apiPath}}{{/checkExtendVerbType}}{{/if}}{{/target}}{{/outgoingRelations}}{{/attached}}')
        headers {
            contentType(applicationJsonUtf8())
        }
        body(
            {{#attached "Event" this}}
            {{#outgoingRelations}}
            {{#target}}
            {{#examples}}
            {{#when}}
            {{#each value}}
                {{@key}}: {{this}},
            {{/each}}
            {{/when}}
            {{/examples}}
            {{/target}}
            {{/outgoingRelations}}
            {{/attached}}
        )
    }
    response {
        status 200
        body(
            {{#attached "Event" this}}
            {{#outgoingRelations}}
            {{#target}}
            {{#examples}}
            {{#then}}
            {{#each value}}
                {{@key}}: {{this}},
            {{/each}}
            {{/then}}
            {{/examples}}
            {{/target}}
            {{/outgoingRelations}}
            {{/attached}}
        )
        bodyMatchers {
            {{#attached "Event" this}}
            {{#outgoingRelations}}
            {{#target}}
            {{#examples}}
            {{#then}}
            {{#each value}}
            jsonPath('$.{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this ../../../aggregateList ../../../aggregate}}{{/setExampleType}}())
            {{/each}}
            {{/then}}
            {{/examples}}
            {{/target}}
            {{/outgoingRelations}}
            {{/attached}}
        }
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}


<function>
    window.$HandleBars.registerHelper('checkOutgoing', function (target) {
        if(target.type == 'Command' && target.examples){
            return false;
        }
        return true;
    })

    window.$HandleBars.registerHelper('setExampleType', function (key, value, aggregateList, aggregate) {
        var type = 'String'
        if(aggregateList){
            for(var i = 0; i < aggregateList.length; i++){
                for(var j = 0; j< aggregateList[i].aggregateRoot.fieldDescriptors.length; j++){
                    if(aggregateList[i].aggregateRoot.fieldDescriptors[j].name == key){
                        type = aggregateList[i].aggregateRoot.fieldDescriptors[j].className
                    }
                }
                
            }
        }else if(!aggregateList && aggregate){
            for(var i = 0; i < aggregate.aggregateRoot.fieldDescriptors.length; i++){
                if(aggregate.aggregateRoot.fieldDescriptors[i].name == key){
                    type = aggregate.aggregateRoot.fieldDescriptors[i].className
                }
            }
        }
        
        return type;
    })

    window.$HandleBars.registerHelper('checkExtendVerbType', function (type, path) {
        if(type == 'POST'){
            return path;
        }else{
            return '/1/'+ path;
        }
    })
</function>