package ru.click.solrj;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class SolrJTestService {

    static final Logger logger = LogManager.getLogger(SolrJTestService.class);
    private static final String solrUrl = "http://10.10.17.114:8983/solr/test";
    private final SolrClient solrClient;
    private final Lorem lorem = LoremIpsum.getInstance();

    public SolrJTestService() {
        solrClient = new HttpSolrClient.Builder(solrUrl)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }

    public void addDoc(String id) {

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", id);
        doc.addField("label", "test");
        doc.addField("first_name", lorem.getFirstName());
        doc.addField("last_name", lorem.getLastName());

        addDoc(doc);
    }

    public void removeDoc(String id) {
        try {
            UpdateResponse response = solrClient.deleteById(id);
            logger.info("Remove response: " + response);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            logger.error("Cannot remove document from solr.", e);
        }
    }

    public void atomicUpdate(String operation, String parentId, String childId) {
        atomicUpdate(operation, null, parentId, childId);
    }

    public void atomicUpdate(String operation, String rootId, String parentId, String childId) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", parentId);
        if (rootId != null) {
            doc.addField("_root_", rootId);
        } else {
            doc.addField("_root_", parentId);
        }
        // doc.addField("friends", "{\"add\":{\"id\": \"" + idChild + "\"}}");
        HashMap<String, Object> entry = new HashMap<>(1);

        SolrInputDocument childDoc = new SolrInputDocument();
       /* if (rootId != null) {
            childDoc.addField("_root_", rootId);
        }*/
        childDoc.addField("id", childId);
        childDoc.addField("first_name", lorem.getFirstName());
        childDoc.addField("last_name", lorem.getLastName());

        entry.put(operation, childDoc);

        doc.addField("friends", entry);
        //  doc.addField("enemies", entry);
        addDoc(doc);
    }

    public void atomicRemove(String parentId, String childId) {
        atomicRemove(null, parentId, childId);
    }

    public void atomicRemove(String rootId, String parentId, String childId) {

        rootId = (rootId == null)? parentId : rootId;

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", parentId);
        doc.addField("_root_", rootId);
        HashMap<String, Object> entry = new HashMap<>(1);

        SolrInputDocument child = new SolrInputDocument();
        child.addField("id", childId);

        entry.put("remove", child);

        doc.addField("friends", entry);

        addDoc(doc);
    }





    private void addDoc(SolrInputDocument doc) {
        try {
            UpdateResponse response = solrClient.add(doc);
            logger.info("Add response: " + response);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            logger.error("Cannot add document to solr.", e);
        }
    }


}
