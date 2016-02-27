package com.compremelhor.model.util;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class ArquillianWarnUtils {
	private static final String BEANS_XML = "beans.xml";
	private static final String ASSERTJ_COORDINATE = 
			"org.assertj:assertj-core";
	private static File[] ASSERTJ_ARTIFACT = Maven.resolver()
			.loadPomFromFile("pom.xml")
			.resolve(ASSERTJ_COORDINATE)
			.withTransitivity().asFile();
	
	public static WebArchive getBasicWebArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addAsResource("META-INF/persistence.xml")   
				.addAsLibraries(ASSERTJ_ARTIFACT)
				.addAsWebInfResource(EmptyAsset.INSTANCE, BEANS_XML);
	}
}
