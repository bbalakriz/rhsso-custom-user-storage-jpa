package com.redhat.custom.storage.user;

/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */

public class UserStoreQueries {
	// User retrieval queries

	public final static String GET_USER_BY_ID =
			"select userName,phone,email,fullName from UserStore where id = ?1";

	public final static String GET_USER_BY_NAME =
			"select userName,phone,email,fullName from UserStore where userName = ?1";

	public final static String GET_ALL_USERS =
			"select userName,phone,email,fullName from UserStore";

	public final static String GET_USER_COUNT = "select count(userName) from UserStore";

	public final static String SEARCH_USER_BY_NAME =
			"select userName,phone,email,fullName from UserStore where userName like ?1";

	// User authentication queries
	public final static String AUTH_USER_PASS =
			"select b.userName, b.passPhrase from UserStore a, UserPass b where a.userName=b.userName and b.passPhrase=?1 and a.userName= ?2";

	// Stored Procedure to get user by user name
	public final static String GET_USER_BY_NAME_SP = "GetUserByName";
	public final static String GET_USER_BY_NAME_SP_ARG1 = "user_name";

	// User authentication stored procedure
	public final static String AUTHENITCATE_USER_SP = "GetUserByName";
	public final static String AUTHENITCATE_USER_SP_ARG1 = "i_agent_code";
	public final static String AUTHENITCATE_USER_SP_ARG2 = "i_password";

}
