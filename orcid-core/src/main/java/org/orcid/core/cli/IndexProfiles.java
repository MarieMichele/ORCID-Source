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
package org.orcid.core.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.orcid.core.manager.OrcidIndexManager;
import org.orcid.core.manager.OrcidProfileManager;
import org.orcid.jaxb.model.message.OrcidProfile;
import org.orcid.persistence.dao.ProfileDao;
import org.orcid.persistence.jpa.entities.IndexingStatus;
import org.orcid.utils.NullUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Will Simpson
 * 
 */
public class IndexProfiles {

    private static Logger LOG = LoggerFactory.getLogger(IndexProfiles.class);

    private OrcidProfileManager orcidProfileManager;
    private OrcidIndexManager orcidIndexManager;
    private ProfileDao profileDao;
    @Option(name = "-f", usage = "Path to file containing ORCIDs to index")
    private File fileToLoad;
    @Option(name = "-o", usage = "ORCID to index")
    private String orcid;
    @Option(name = "-c", usage = "Continue to next record if there is an error (default = stop on error)")
    private boolean continueOnError;
    private int doneCount;
    private int errorCount;

    public static void main(String[] args) throws IOException {
        IndexProfiles indexProfiles = new IndexProfiles();
        CmdLineParser parser = new CmdLineParser(indexProfiles);
        try {
            parser.parseArgument(args);
            indexProfiles.validateArgs(parser);
            indexProfiles.init();
            indexProfiles.execute();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        } catch (Throwable t) {
            System.err.println(t);
            System.exit(2);
        }
        System.exit(0);
    }

    private void validateArgs(CmdLineParser parser) throws CmdLineException {
        if (NullUtils.allNull(fileToLoad, orcid)) {
            throw new CmdLineException(parser, "At least one of -f | -o must be specificed");
        }
    }

    public void execute() throws IOException {
        if (fileToLoad != null) {
            processFile();
        }
        if (orcid != null) {
            processOrcid(orcid);
        }
    }

    private void processFile() throws IOException {
        long startTime = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(fileToLoad))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line)) {
                    processOrcid(line.trim());
                }
            }
            long endTime = System.currentTimeMillis();
            String timeTaken = DurationFormatUtils.formatDurationHMS(endTime - startTime);
            LOG.info("Finished resaving profiles: doneCount={}, errorCount={}, timeTaken={} (H:m:s.S)", new Object[] { doneCount, errorCount, timeTaken });
        }
    }

    private void processOrcid(final String orcid) {
        LOG.info("Indexing profile: {}", orcid);
        try {
            OrcidProfile orcidProfile = orcidProfileManager.retrieveClaimedOrcidProfile(orcid);
            orcidIndexManager.persistProfileInformationForIndexing(orcidProfile);
            profileDao.updateIndexingStatus(orcid, IndexingStatus.DONE);
        } catch (RuntimeException e) {
            errorCount++;
            if (continueOnError) {
                LOG.error("Error indexing profile: orcid={}", orcid, e);
                return;
            } else {
                throw e;
            }
        }
        doneCount++;
    }

    private void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("orcid-core-context.xml");
        orcidProfileManager = (OrcidProfileManager) context.getBean("orcidProfileManager");
        orcidIndexManager = (OrcidIndexManager) context.getBean("orcidIndexManager");
        profileDao = (ProfileDao) context.getBean("profileDao");
    }

}
