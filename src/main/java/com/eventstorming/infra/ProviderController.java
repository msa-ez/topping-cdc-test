forEach: Aggregate
fileName: {{namePascalCase}}Controller.java
path: {{boundedContext.name}}/{{options.packagePath}}/infra
except: {{#attached "Event" this}}{{#incomingRelations}}{{#source}}{{#checkIncoming examples}}{{/checkIncoming}}{{/source}}{{/incomingRelations}}{{/attached}}
---
package {{options.package}}.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import {{options.package}}.domain.{{namePascalCase}}Repository;

@RestController
public class {{namePascalCase}}Controller {

    @Autowired
    {{namePascalCase}}Repository {{nameCamelCase}}Repository;

    {{#commands}}
    {{#isRestRepository}}
    {{/isRestRepository}}

    {{^isRestRepository}}
    {{#checkMethod controllerInfo.method}}
    @RequestMapping(value = "{{../namePlural}}/{id}/{{controllerInfo.apiPath}}",
        method = RequestMethod.{{controllerInfo.method}},
        produces = "application/json;charset=UTF-8")
    public {{../namePascalCase}} {{nameCamelCase}}(@PathVariable(value = "id") {{../keyFieldDescriptor.className}} id, {{#if (hasFields fieldDescriptors)}}@RequestBody {{namePascalCase}}Command {{nameCamelCase}}Command, {{/if}}HttpServletRequest request, HttpServletResponse response) throws Exception {
            System.out.println("##### /{{../nameCamelCase}}/{{nameCamelCase}}  called #####");
            Optional<{{../namePascalCase}}> optional{{../namePascalCase}} = {{../nameCamelCase}}Repository.findById(id);
            
            optional{{../namePascalCase}}.orElseThrow(()-> new Exception("No Entity Found"));
            {{../namePascalCase}} {{../nameCamelCase}} = optional{{../namePascalCase}}.get();
            {{../nameCamelCase}}.{{nameCamelCase}}({{#if (hasFields fieldDescriptors)}}{{nameCamelCase}}Command{{/if}});
            
            {{../nameCamelCase}}Repository.{{#methodConvert controllerInfo.method}}{{/methodConvert}}({{../nameCamelCase}});
            return {{../nameCamelCase}};
            
    }
    
    {{#each ../aggregateRoot.entities.relations}}
    {{#if (isGeneralization targetElement.namePascalCase ../../namePascalCase relationType)}}
    @RequestMapping(value = "{{#toURL sourceElement.nameCamelCase}}{{/toURL}}/{id}/{{../controllerInfo.apiPath}}",
            method = RequestMethod.{{../controllerInfo.method}},
            produces = "application/json;charset=UTF-8")
    public {{../../namePascalCase}} {{../nameCamelCase}}{{sourceElement.namePascalCase}}(
        @PathVariable(value = "id") {{../../keyFieldDescriptor.className}} id, {{#if (hasFields ../fieldDescriptors)}}@RequestBody {{../namePascalCase}}Command {{../nameCamelCase}}Command, {{/if}}HttpServletRequest request, HttpServletResponse response) throws Exception {
            return {{../nameCamelCase}}(id, {{#if (hasFields ../fieldDescriptors)}}{{../nameCamelCase}}Command,{{/if}} request, response);
    }
    {{/if}}
    {{/each}}

    {{/checkMethod}}

    {{^checkMethod controllerInfo.method}}
    @RequestMapping(value = "{{../namePlural}}/{{controllerInfo.apiPath}}",
            method = RequestMethod.{{controllerInfo.method}},
            produces = "application/json;charset=UTF-8")
    public {{../namePascalCase}} {{nameCamelCase}}(HttpServletRequest request, HttpServletResponse response, 
        {{#if fieldDescriptors}}@RequestBody {{aggregate.namePascalCase}} {{aggregate.nameCamelCase}}{{/if}}) throws Exception {
            System.out.println("##### /{{aggregate.nameCamelCase}}/{{nameCamelCase}}  called #####");
            {{aggregate.nameCamelCase}}.{{nameCamelCase}}({{#if fieldDescriptors}}{{nameCamelCase}}command{{/if}});
            {{aggregate.nameCamelCase}}Repository.save({{aggregate.nameCamelCase}});
            return {{aggregate.nameCamelCase}};
    }
    {{/checkMethod}}    
    {{/isRestRepository}}
    {{/commands}}

    {{#attached "Command" this}}
    {{#if controllerInfo.apiPath}}
    {{else}}
    @GetMapping("/{{namePlural}}/{id}")
    public ResponseEntity<{{namePascalCase}}> {{nameCamelCase}}StockCheck(@PathVariable(value = "id") Long id) {
        return {{nameCamelCase}}Repository.findById(id)
            .map({{nameCamelCase}} -> ResponseEntity.ok().body({{nameCamelCase}}))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    {{/if}}
    {{/attached}}

    
}
<function>
    window.$HandleBars.registerHelper('checkIncoming', function (examples) {
        if(!examples) return true;
    })
    window.$HandleBars.registerHelper('checkMethod', function (method, options) {
        if(method.endsWith("PUT") || method.endsWith("DELETE")){
            return options.fn(this);
        } else {
            return options.inverse(this);
        }
    });
</function>
