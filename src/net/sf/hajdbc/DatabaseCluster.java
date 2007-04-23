/*
 * HA-JDBC: High-Availability JDBC
 * Copyright (c) 2004-2007 Paul Ferraro
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your 
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contact: ferraro@users.sourceforge.net
 */
package net.sf.hajdbc;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author Paul Ferraro
 * @param <D> either java.sql.Driver or javax.sql.DataSource
 */
public interface DatabaseCluster<D>
{
	public String getId();
	
	/**
	 * Activates the specified database
	 * @param database a database descriptor
	 * @return true, if the database was activated, false it was already active
	 */
	public boolean activate(Database<D> database);
	
	/**
	 * Deactivates the specified database
	 * @param database a database descriptor
	 * @return true, if the database was deactivated, false it was already inactive
	 */
	public boolean deactivate(Database<D> database);
	
	/**
	 * Returns a map of database to connection factory for this obtaining connections to databases in this cluster.
	 * @return a connection factory map
	 */
	public Map<Database<D>, D> getConnectionFactoryMap();
	
	/**
	 * Determines whether or not the specified database is responding
	 * @param database a database descriptor
	 * @return true, if the database is responding, false if it appears down
	 */
	public boolean isAlive(Database<D> database);
	
	/**
	 * Returns the database identified by the specified id
	 * @param id a database identifier
	 * @return a database descriptor
	 * @throws IllegalArgumentException if no database exists with the specified identifier
	 */
	public Database<D> getDatabase(String id);

	/**
	 * Handles a failure caused by the specified cause on the specified database.
	 * If the database is not alive, then it is deactivated, otherwise an exception is thrown back to the caller.
	 * @param database a database descriptor
	 * @param cause the cause of the failure
	 * @throws SQLException if the database is alive
	 */
	public void handleFailure(Database<D> database, SQLException cause) throws SQLException;
	
	/**
	 * Returns the Balancer implementation used by this database cluster.
	 * @return an implementation of <code>Balancer</code>
	 */
	public Balancer<D> getBalancer();
	
	/**
	 * Returns an executor service used to execute transactional database writes.
	 * @return an implementation of <code>ExecutorService</code>
	 * @since 1.1
	 */
	public ExecutorService getTransactionalExecutor();
	
	/**
	 * Returns an executor service used to execute non-transactional database writes.
	 * @return an implementation of <code>ExecutorService</code>
	 * @since 1.1
	 */
	public ExecutorService getNonTransactionalExecutor();
	
	/**
	 * Returns a dialect capable of returning database vendor specific values.
	 * @return an implementation of <code>Dialect</code>
	 * @since 1.1
	 */
	public Dialect getDialect();
	
	/**
	 * Returns a LockManager capable of acquiring named read/write locks on the specific objects in this database cluster.
	 * @return a LockManager implementation
	 * @since 2.0
	 */
	public LockManager getLockManager();
	
	/**
	 * Sets the LockManager implementation capable of acquiring named read/write locks on the specific objects in this database cluster.
	 * @since 2.0
	 */
	public void setLockManager(LockManager lockManager);
	
	/**
	 * Returns a StateManager for persisting database cluster state.
	 * @return a StateManager implementation
	 * @since 2.0
	 */
	public StateManager getStateManager();
	
	/**
	 * Sets the StateManager implementation for persisting database cluster state.
	 * @since 2.0
	 */
	public void setStateManager(StateManager stateManager);
	
	/**
	 * Starts this database cluster.
	 * @throws Exception if database cluster fails to start
	 * @since 1.1
	 */
	public void start() throws Exception;
	
	/**
	 * Stops this database cluster.
	 * @since 1.1
	 */
	public void stop();
	
	/**
	 * Returns a DatabaseMetaData cache.
	 * @return a <code>DatabaseMetaDataCache</code> implementation
	 * @since 2.0
	 */
	public DatabaseMetaDataCache getDatabaseMetaDataCache();
	
	public boolean isSequenceDetectionEnabled();
	
	public boolean isIdentityColumnDetectionEnabled();
}
