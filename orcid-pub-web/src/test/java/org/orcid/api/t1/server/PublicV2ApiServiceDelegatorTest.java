/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.api.t1.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.api.t1.server.delegator.PublicV2ApiServiceDelegator;
import org.orcid.jaxb.model.common.Visibility;
import org.orcid.jaxb.model.record.Education;
import org.orcid.jaxb.model.record.Employment;
import org.orcid.jaxb.model.record.Funding;
import org.orcid.jaxb.model.record.Work;
import org.orcid.jaxb.model.record.WorkType;
import org.orcid.jaxb.model.record.summary.ActivitiesSummary;
import org.orcid.test.DBUnitTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:orcid-t1-web-context.xml", "classpath:orcid-t1-security-context.xml" })
public class PublicV2ApiServiceDelegatorTest extends DBUnitTest {
    private static final List<String> DATA_FILES = Arrays.asList("/data/EmptyEntityData.xml", "/data/SecurityQuestionEntityData.xml",
            "/data/SourceClientDetailsEntityData.xml", "/data/ProfileEntityData.xml", "/data/WorksEntityData.xml", "/data/ProfileWorksEntityData.xml",
            "/data/ClientDetailsEntityData.xml", "/data/Oauth2TokenDetailsData.xml", "/data/OrgsEntityData.xml", "/data/ProfileFundingEntityData.xml", "/data/OrgAffiliationEntityData.xml");
    
    @Resource(name = "publicV2ApiServiceDelegator")
    PublicV2ApiServiceDelegator serviceDelegator;
    
    @BeforeClass
    public static void initDBUnitData() throws Exception {
        initDBUnitData(DATA_FILES);
    }
    
    @AfterClass
    public static void removeDBUnitData() throws Exception {
        Collections.reverse(DATA_FILES);
        removeDBUnitData(DATA_FILES);
    }
    
    @Test
    public void testFindWorksDetails() {
        Response response = serviceDelegator.viewActivities("4444-4444-4444-4446");
        assertNotNull(response);
        ActivitiesSummary summary = (ActivitiesSummary) response.getEntity();
        assertNotNull(summary);
        // Check works
        assertNotNull(summary.getWorks());
        assertEquals(1, summary.getWorks().getWorkGroup().size());
        assertEquals("/4444-4444-4444-4446/work/5", summary.getWorks().getWorkGroup().get(0).getWorkSummary().get(0).getPutCode());
        assertEquals("Journal article A", summary.getWorks().getWorkGroup().get(0).getWorkSummary().get(0).getTitle().getTitle().getContent());
        
        // Check fundings
        assertNotNull(summary.getFundings());
        assertEquals(1, summary.getFundings().getFundingGroup().size());
        assertEquals("/4444-4444-4444-4446/funding/5", summary.getFundings().getFundingGroup().get(0).getFundingSummary().get(0).getPutCode());
        assertEquals("Public Funding", summary.getFundings().getFundingGroup().get(0).getFundingSummary().get(0).getTitle().getTitle().getContent());
        
        // Check Educations
        assertNotNull(summary.getEducations());
        assertNotNull(summary.getEducations().getSummaries());
        assertEquals(1, summary.getEducations().getSummaries().size());
        assertEquals("/4444-4444-4444-4446/education/7", summary.getEducations().getSummaries().get(0).getPutCode());
        assertEquals("Education Dept # 2", summary.getEducations().getSummaries().get(0).getDepartmentName());

        // Check Employments
        assertNotNull(summary.getEmployments());
        assertNotNull(summary.getEmployments().getSummaries());
        assertEquals(1, summary.getEmployments().getSummaries().size());
        assertEquals("/4444-4444-4444-4446/employment/8", summary.getEmployments().getSummaries().get(0).getPutCode());
        assertEquals("Employment Dept # 2", summary.getEmployments().getSummaries().get(0).getDepartmentName());
    }

    @Test
    public void testViewWork() {
        Response response = serviceDelegator.viewWork("4444-4444-4444-4446", "5");
        assertNotNull(response);
        Work work = (Work) response.getEntity();
        assertNotNull(work);
        assertNotNull(work.getWorkTitle());
        assertNotNull(work.getWorkTitle().getTitle());
        assertEquals("Journal article A", work.getWorkTitle().getTitle().getContent());
        assertEquals("/4444-4444-4444-4446/work/5", work.getPutCode());
        assertEquals(WorkType.JOURNAL_ARTICLE, work.getWorkType());
    }

    @Test
    public void testViewFunding() {
        Response response = serviceDelegator.viewFunding("4444-4444-4444-4446", "5");
        assertNotNull(response);
        Funding funding = (Funding) response.getEntity();
        assertNotNull(funding);
        assertNotNull(funding.getTitle());
        assertNotNull(funding.getTitle().getTitle());
        assertEquals("Public Funding", funding.getTitle().getTitle().getContent());
        assertEquals(Visibility.PUBLIC.value(), funding.getVisibility().value());
    }

    @Test
    public void testViewEducation() {
        Response response = serviceDelegator.viewEducation("4444-4444-4444-4446", "7");
        assertNotNull(response);
        Education education = (Education) response.getEntity();
        assertNotNull(education);
        assertEquals("Education Dept # 2", education.getDepartmentName());
        assertEquals(Visibility.PUBLIC.value(), education.getVisibility().value());
    }

    @Test
    public void testViewEmployment() {
        Response response = serviceDelegator.viewEmployment("4444-4444-4444-4446", "8");
        assertNotNull(response);
        Employment employment = (Employment) response.getEntity();
        assertNotNull(employment);
        assertEquals("Employment Dept # 2", employment.getDepartmentName());
        assertEquals(Visibility.PUBLIC.value(), employment.getVisibility().value());
    }
}
