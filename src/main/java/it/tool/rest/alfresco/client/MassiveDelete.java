package it.tool.rest.alfresco.client;


import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class.
 * 
 * To parallelize delete operations, you can instantiate more main classes over different deletion sets.
 * 
 * @author Francesco Fornasari - Christian Tiralosi T.A.I Software Solution s.r.l
 */
public class MassiveDelete {

    private static final Logger logger = LoggerFactory.getLogger(MassiveDelete.class);
    private static AlfrescoRestUtils utils;
    
    public static void main(String arg[]){
   
        ApplicationContext context = new ClassPathXmlApplicationContext("application-bean.xml");
        utils = (AlfrescoRestUtils) context.getBean("restUtils");
        if (arg.length != 0){
            if (!arg[0].isEmpty()){
                String pathfile = arg[0];
                logger.debug("Using Alfresco context [" +utils.getAlfrescoContextUrl() + "]");
                logger.debug("Using uuid list file [" + pathfile + "]");
                MassiveDelete md = new MassiveDelete();
                md.delete(pathfile);
            } else {
                logger.debug("Usage: MassiveDelete path-to-file");
            }
        } else {
            logger.debug("Usage: MassiveDelete path-to-file");
        }
    }
    
    public MassiveDelete(){}
    
    public void delete(String pathfile){
        
        File nodeRefListFile = new File(pathfile);
        LineIterator nodeRefIterator;
        int error = 0;
        int deleted = 0;
        try {
            nodeRefIterator = FileUtils.lineIterator(nodeRefListFile);
        } catch (IOException ex) {
            logger.error("Error getting uuid list: ", ex);
            return;
        }
        
        String uuid;
        while (nodeRefIterator.hasNext()) {

            uuid = nodeRefIterator.nextLine();
            if (logger.isDebugEnabled()) {
                logger.debug("Node to delete: " + uuid);
            }
            if (!uuid.equals("")) {
                try {
                    utils.deleteNode(uuid.trim());
                    deleted++;
                } catch (Exception ex) {
                    logger.error(ex.toString());
                    error++;
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Stats: deleted[" + deleted + "] error[" + error+"]");
            }
        }
    }
    
}
