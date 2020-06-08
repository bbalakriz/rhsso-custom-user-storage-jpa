package com.redhat.custom.storage.user;

import javax.naming.InitialContext;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */
public class CustomUserStorageProviderFactory
		implements UserStorageProviderFactory<ILocalCustomUserStorageProvider> {
	private static final Logger logger = Logger.getLogger(CustomUserStorageProviderFactory.class);

	@Override
	public ILocalCustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {
		try {
			InitialContext ctx = new InitialContext();
			ILocalCustomUserStorageProvider provider = (ILocalCustomUserStorageProvider) ctx
					.lookup("java:global/custom-user-storage-jpa/"
							+ CustomUserStorageProvider.class.getSimpleName());
			provider.setModel(model);
			provider.setSession(session);
			return provider;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getId() {
		return "mysqldb-userstore";
	}

	@Override
	public String getHelpText() {
		return "MySQL DB User Storage Provider";
	}

	@Override
	public void close() {
		logger.info("<<<<<< Closing factory");

	}
}
