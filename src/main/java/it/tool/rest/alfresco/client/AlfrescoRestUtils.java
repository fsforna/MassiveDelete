package it.tool.rest.alfresco.client;

import org.apache.http.client.methods.HttpDelete;
import org.springframework.beans.factory.InitializingBean;

/**
 * Simple Alfresco Rest Utils
 * 
 * @author Christian Tiralosi T.A.I Software Solution s.r.l
 */
public class AlfrescoRestUtils implements InitializingBean{

    private HttpRestClient client;
    private String alfrescoContextUrl;
    private String user;
    private String password;

    public AlfrescoRestUtils(){}
    
    public AlfrescoRestUtils(HttpRestClient client, String alfrescoContextUrl) {
        this.client = client;
        this.alfrescoContextUrl = alfrescoContextUrl;
    }
    
    public AlfrescoRestUtils(String user, String password, String alfrescoContextUrl){
        this.client = new HttpRestClient(user, password);
        this.alfrescoContextUrl = alfrescoContextUrl;
    }
    
    public void init(){
        this.client = new HttpRestClient(user, password);
    }

    public void deleteNode(String uuid) throws Exception{
        String url = buildUrlDeleterNode(uuid);
        fireGenericDelete(url);
    }
    
    private String buildUrlDeleterNode(String uuid) {
        String basUrl = AlfrescoConstants.urlAlfrescoDeleteNode;
        String relativeUrl = basUrl.replace(AlfrescoConstants.uuidToken, uuid);
        return (alfrescoContextUrl.endsWith("/")) ? alfrescoContextUrl + relativeUrl : alfrescoContextUrl + "/" + relativeUrl;
    }
    
    private void fireGenericDelete(String url) throws Exception {
        HttpDelete delete = new HttpDelete(url);
        client.fireRequest(delete);
    }
    
    public HttpRestClient getClient() {
        return client;
    }

    public void setClient(HttpRestClient client) {
        this.client = client;
    }

    public String getAlfrescoContextUrl() {
        return alfrescoContextUrl;
    }

    public void setAlfrescoContextUrl(String alfrescoContextUrl) {
        this.alfrescoContextUrl = alfrescoContextUrl;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void afterPropertiesSet() throws Exception {}
}
