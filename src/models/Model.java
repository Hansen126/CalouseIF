package models;

import services.Singleton;
import services.Response;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public abstract class Model {
    
    // Abstract methods to be implemented by subclasses
    protected abstract String getTableName();
    protected abstract String getPrimaryKey();
    
    public Model() {
        // Default constructor for future use
    }
    
    /**
     * Retrieve all records from the database table associated with the model.
     *
     * @param <T>    The type parameter representing the model class.
     * @param model  The Class object of the model.
     * @return       A Response object containing a list of all records.
     */
    protected <T> Response<ArrayList<T>> fetchAll(Class<T> model){
        Response<ArrayList<T>> response = new Response<>();
        
        try {
            // Initialize a list to store instances of T
            ArrayList<T> records = new ArrayList<>();
            // Get the database connection singleton instance
            Singleton dbConnection = Singleton.getInstance();
            // Prepare the SQL query to retrieve all rows from the specified table
            String query = "SELECT * FROM " + getTableName();
            PreparedStatement ps = dbConnection.prepareStatement(query);
            ResultSet rs = dbConnection.executeQuery(ps);
            
            // Iterate through the result set
            while (rs.next()) {
                // Create a new instance of T using reflection
                T instance = model.getDeclaredConstructor().newInstance();
                // Retrieve all declared fields of the instance's class
                Field[] fields = instance.getClass().getDeclaredFields();
                
                for (Field field : fields) {
                    // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                    if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                        continue;
                    }
                    // Make the field accessible for reflection
                    field.setAccessible(true);
                    // Retrieve the field's value from the current row in the result set
                    Object value = rs.getObject(field.getName());
                    // Assign the retrieved value to the field in the instance
                    field.set(instance, value);
                }
                records.add(instance);
            }
            
            response.setMessage("Success: Retrieved all records.");
            response.setSuccess(true);
            response.setData(records);
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            response.setMessage("Database Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }

    /**
     * Retrieve the most recently added record from the database table.
     *
     * @param <T>    The type parameter representing the model class.
     * @param model  The Class object of the model.
     * @return       A Response object containing the latest record.
     */
    protected <T> Response<T> fetchLatest(Class<T> model) {
        Response<T> response = new Response<>();
    
        try {
            // SQL query to select the latest record based on the primary key
            String query = "SELECT * FROM " + getTableName() + " ORDER BY " + getPrimaryKey() + " DESC LIMIT 1";
        
            // Get the database connection singleton instance
            Singleton dbConnection = Singleton.getInstance();
            PreparedStatement ps = dbConnection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Create a new instance of T using reflection
                T instance = model.getDeclaredConstructor().newInstance();
                Field[] fields = instance.getClass().getDeclaredFields();
                
                for (Field field : fields) {
                    // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                    if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                        continue;
                    }
                    // Make the field accessible for reflection
                    field.setAccessible(true);
                    // Retrieve the field's value from the current row in the result set
                    Object value = rs.getObject(field.getName());
                    // Assign the retrieved value to the field in the instance
                    field.set(instance, value);
                }
                response.setMessage("Success: Retrieved the latest record.");
                response.setSuccess(true);
                response.setData(instance);
                return response;
            } else {
                response.setMessage("No records found.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setMessage("Database Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }
    
    /**
     * Find a specific record by its primary key.
     *
     * @param <T>      The type parameter representing the model class.
     * @param model    The Class object of the model.
     * @param primaryKeyValue The value of the primary key to search for.
     * @return         A Response object containing the found record.
     */
    protected <T> Response<T> findByPrimaryKey(Class<T> model, String primaryKeyValue){    
        Response<T> response = new Response<>();
        
        try {
            // Prepare a SQL query to select a record based on the primary key
            String query = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKey() + " = ?";
            Singleton dbConnection = Singleton.getInstance();
            PreparedStatement ps = dbConnection.prepareStatement(query);
            ps.setString(1, primaryKeyValue);
            ResultSet rs = dbConnection.executeQuery(ps);
            
            if (rs.next()) {
                // Create a new instance of T using reflection
                T instance = model.getDeclaredConstructor().newInstance();
                Field[] fields = instance.getClass().getDeclaredFields();
                
                for (Field field : fields) {
                    // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                    if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                        continue;
                    }
                    // Make the field accessible for reflection
                    field.setAccessible(true);
                    // Retrieve the field's value from the current row in the result set
                    Object value = rs.getObject(field.getName());
                    // Assign the retrieved value to the field in the instance
                    field.set(instance, value);
                }
                response.setMessage("Success: Record found.");
                response.setSuccess(true);
                response.setData(instance);
                return response;
            } else {
                response.setMessage("Error: Record not found.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setMessage("Database Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }
    
    
    /**
     * Insert a new record into the database table.
     *
     * @param <T>    The type parameter representing the model class.
     * @param model  The Class object of the model.
     * @return       A Response object containing the inserted record.
     */
//    protected <T> Response<T> insertRecord(Class<T> model) {
//        Response<T> response = new Response<>();
//        System.out.println("insertRecord 1");
//
//        try {
//            // Explicitly define the field order for the columns
//            LinkedHashMap<String, Object> fieldMap = new LinkedHashMap<>();
//            
//            // Reflect and fetch all fields, but insert in controlled order
//            fieldMap.put("transactionId", null);
//            fieldMap.put("userId", null);
//            fieldMap.put("productId", null);
//
//            Field[] fields = this.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                if (fieldMap.containsKey(field.getName())) {
//                    fieldMap.put(field.getName(), field.get(this));
//                }
//            }
//
//            // Prepare the SQL query
//            String columns = String.join(", ", fieldMap.keySet());
//            String placeholders = String.join(", ", Collections.nCopies(fieldMap.size(), "?"));
//            String query = "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";
//
//            System.out.println("Query: " + query);
//            ConnectionSingleton dbConnection = ConnectionSingleton.getInstance();
//            PreparedStatement ps = dbConnection.prepareStatement(query);
//
//            // Set the values in the correct order
//            int i = 1;
//            for (Object value : fieldMap.values()) {
//                if (value == null) {
//                    ps.setNull(i, java.sql.Types.VARCHAR);
//                } else {
//                    ps.setObject(i, value);
//                }
//                i++;
//            }
//
//            // Execute update
//            dbConnection.executeUpdate(ps);
//            System.out.println("Record inserted successfully.");
//
//            response.setMessage("Success: Record inserted successfully.");
//            response.setSuccess(true);
//            response.setData((T) this);
//            return response;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.setMessage("Database Error: " + e.getMessage());
//            response.setSuccess(false);
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setMessage("Error: " + e.getMessage());
//            response.setSuccess(false);
//            return response;
//        }
//    }
    
    protected <T> Response<T> insertRecord(Class<T> model){
    	
        Response<T> response = new Response<>();
        System.out.println("inserRecord 1");
        
        try {
            // Construct the list of column names and value placeholders for the SQL query
            StringBuilder columns = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();
            ArrayList<Object> parameters = new ArrayList<Object>();
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(this);
                
                if(columns.length() > 0) {
                    columns.append(", ");
                    placeholders.append(", ");
                }
                columns.append(field.getName());
                placeholders.append("?");
                parameters.add(value == null ? null : value);
            }
            System.out.println("inserRecord 6");
            Singleton dbConnection = Singleton.getInstance();
            System.out.println("inserRecord 7");
            // Construct the SQL INSERT query
            String query;
            if(model.equals(Transaction.class)) {
        		System.out.println("trueee");
        		query = "INSERT INTO " + getTableName() + " (" + "userId, productId, transactionId" + ") VALUES (" + placeholders + ")";
        	} else {
        		query = "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";
        	}
            
            System.out.println(query);
            System.out.println(columns);
            System.out.println(placeholders);
            PreparedStatement ps = dbConnection.prepareStatement(query);    
            System.out.println("inserRecord 8");
            // Set the values for the prepared statement using the parameters list
            for(int i = 0; i < parameters.size(); i++) {
                if(parameters.get(i) == null) {
                    // If the value is null, set the parameter to SQL NULL
                    ps.setNull(i + 1, java.sql.Types.VARCHAR);
                } else {
                    // Set the parameter to the corresponding object value
                    ps.setObject(i + 1, parameters.get(i));                    
                }
            }
            System.out.println("inserRecord 9");
            dbConnection.executeUpdate(ps);
            System.out.println("inserRecord 10");
            response.setMessage("Success: Record inserted successfully.");
            response.setSuccess(true);
            response.setData((T) this);
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            response.setMessage("Database Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }
    
    
    /**
     * Update an existing record in the database table.
     *
     * @param <T>        The type parameter representing the model class.
     * @param model      The Class object of the model.
     * @param primaryKeyValue The value of the primary key identifying the record to update.
     * @return           A Response object containing the updated record.
     */
    protected <T> Response<T> updateRecord(Class<T> model, String primaryKeyValue) {
        Response<T> response = new Response<>();
        
        try {
            // Construct the SET clause of the SQL UPDATE query
            StringBuilder setClause = new StringBuilder();
            ArrayList<Object> parameters = new ArrayList<>();
            
            Field[] fields = this.getClass().getDeclaredFields();
            
            // Ensure that the primary key is not null before proceeding
            if(this.getPrimaryKey() == null) {
                response.setMessage("Error: Primary key is null.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }
            
            for (Field field : fields) {
                // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(this);
                
                if(!field.getName().equalsIgnoreCase("id")) {
                    // Append the field name and value placeholder to the SET clause
                    if(setClause.length() > 0) {
                        setClause.append(", ");
                    }
                    setClause.append(field.getName()).append(" = ?");
                    parameters.add(value);
                }                
            }
            
            // Add the primary key value as the last parameter for the WHERE clause
            parameters.add(primaryKeyValue);
            
            Singleton dbConnection = Singleton.getInstance();
            // Construct the SQL UPDATE query
            String query = "UPDATE " + getTableName() + " SET " + setClause + " WHERE " + getPrimaryKey() + " = ?";
            PreparedStatement ps = dbConnection.prepareStatement(query);    
            
            for(int i = 0; i < parameters.size(); i++) {
                if(parameters.get(i) == null) {
                    ps.setNull(i + 1, java.sql.Types.VARCHAR);
                } else {
                    ps.setObject(i + 1, parameters.get(i));                    
                }
            }
            
            dbConnection.executeUpdate(ps);
            response.setMessage("Success: Record updated successfully.");
            response.setSuccess(true);
            response.setData((T) this);
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            response.setMessage("Database Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;    
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }
    
    /**
     * Delete a record from the database table based on the primary key.
     *
     * @param <T>      The type parameter representing the model class.
     * @param model    The Class object of the model.
     * @param primaryKeyValue The value of the primary key identifying the record to delete.
     * @return         A Response object indicating the success or failure of the deletion.
     */
    protected <T> Response<Boolean> deleteRecord(Class<T> model, String primaryKeyValue) {    
        Response<Boolean> response = new Response<>();
        
        try {
            String query = "DELETE FROM " + getTableName() + " WHERE " + getPrimaryKey() + " = ?";
            
            Singleton dbConnection = Singleton.getInstance();
            PreparedStatement ps = dbConnection.prepareStatement(query);
            ps.setString(1, primaryKeyValue);
            int affectedRows = dbConnection.executeUpdate(ps);
            
            if(affectedRows > 0) {
                response.setMessage("Success: Record deleted successfully.");
                response.setSuccess(true);
                response.setData(true);
            } else {
                response.setMessage("Error: No records were deleted.");
                response.setSuccess(false);
                response.setData(false);
            }
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            response.setMessage("Database Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(false);
            return response;
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(false);
            return response;
        }
    }
    
    /**
     * Establish a one-to-one relationship between models.
     *
     * @param <T>          The type parameter representing the related model class.
     * @param relatedModel The Class object of the related model.
     * @param relatedTable The name of the related table.
     * @param foreignKey   The foreign key in the related table.
     * @param localKey     The local key in the current model.
     * @return             An instance of the related model.
     */
    protected <T> T hasOne(Class<T> relatedModel, String relatedTable, String localKey, String foreignKey){
        try {
            // Construct the SQL query to find a related record in the target table
        	System.out.println("creating query");
            String query = "SELECT * FROM " + relatedTable + " WHERE " + foreignKey + " = ?;";
            System.out.println(query);
            Singleton dbConnection = Singleton.getInstance();
            PreparedStatement ps = dbConnection.prepareStatement(query);
            ps.setString(1, localKey);
            ResultSet rs = dbConnection.executeQuery(ps);
            
            if(rs.next()) {
                // Create a new instance of T using reflection
                T instance = relatedModel.getDeclaredConstructor().newInstance();
                Field[] fields = instance.getClass().getDeclaredFields();
                
                for (Field field : fields) {
                    // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                    if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                        continue;
                    }
                    // Make the field accessible for reflection
                    field.setAccessible(true);
                    // Retrieve the field's value from the current row in the result set
                    Object value = rs.getObject(field.getName());
                    // Assign the retrieved value to the field in the instance
                    field.set(instance, value);
                }
                return instance;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        
        return null;
    }
    
    /**
     * Establish a one-to-many relationship between models.
     *
     * @param <T>          The type parameter representing the related model class.
     * @param relatedModel The Class object of the related model.
     * @param relatedTable The name of the related table.
     * @param foreignKey   The foreign key in the related table.
     * @param localKey     The local key in the current model.
     * @return             A list of instances of the related model.
     */
    protected <T> ArrayList<T> hasMany(Class<T> relatedModel, String relatedTable, String localKey, String foreignKey){        
        ArrayList<T> relatedRecords = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + relatedTable + " WHERE " + foreignKey + " = ?;";
            Singleton dbConnection = Singleton.getInstance();
            PreparedStatement ps = dbConnection.prepareStatement(query);
            ps.setString(1, localKey);
            ResultSet rs = dbConnection.executeQuery(ps);
            
            while (rs.next()) {
                // Create a new instance of T using reflection
                T instance = relatedModel.getDeclaredConstructor().newInstance();
                Field[] fields = instance.getClass().getDeclaredFields();
                
                for (Field field : fields) {
                    // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                    if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                        continue;
                    }
                    // Make the field accessible for reflection
                    field.setAccessible(true);
                    // Retrieve the field's value from the current row in the result set
                    Object value = rs.getObject(field.getName());
                    // Assign the retrieved value to the field in the instance
                    field.set(instance, value);
                }
                relatedRecords.add(instance);
            }
            
            return relatedRecords;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        
        return relatedRecords;
    }
    
    
    /**
     * Retrieve records where the specified column's value is within the provided list.
     *
     * @param <T>        The type parameter representing the model class.
     * @param model      The Class object of the model.
     * @param columnName The name of the column to filter by.
     * @param values     The list of values to include in the IN clause.
     * @return           A Response object containing a list of matching records.
     */
    protected <T> Response<ArrayList<T>> findWhereIn(Class<T> model, String columnName, ArrayList<String> values) {
        Response<ArrayList<T>> response = new Response<>();
        
        try {
            // Construct placeholders for the IN clause
            StringBuilder placeholders = new StringBuilder();
            for(int i = 0; i < values.size(); i++) {
                if(i > 0) {
                    placeholders.append(", ");
                }
                placeholders.append("?");
            }

            Singleton dbConnection = Singleton.getInstance();
            // SQL query with the dynamically built IN clause
            String query = "SELECT * FROM " + getTableName() + " WHERE " + columnName + " IN (" + placeholders + ")";
            PreparedStatement ps = dbConnection.prepareStatement(query);
            
            // Set the values for the prepared statement using the values list
            for(int i = 0; i < values.size(); i++) {
                ps.setString(i + 1, values.get(i));
            }

            ResultSet rs = dbConnection.executeQuery(ps);
            ArrayList<T> records = new ArrayList<>();
            
            while (rs.next()) {
                // Create a new instance of T using reflection
                T instance = model.getDeclaredConstructor().newInstance();
                Field[] fields = instance.getClass().getDeclaredFields();
                
                for (Field field : fields) {
                    // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                    if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                        continue;
                    }
                    // Make the field accessible for reflection
                    field.setAccessible(true);
                    // Retrieve the field's value from the current row in the result set
                    Object value = rs.getObject(field.getName());
                    // Assign the retrieved value to the field in the instance
                    field.set(instance, value);
                }
                records.add(instance);
            }

            response.setMessage("Success: Retrieved records matching the criteria.");
            response.setSuccess(true);
            response.setData(records);
            return response;
        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }
    
    
    /**
     * Retrieve records based on a specific condition.
     *
     * @param <T>         The type parameter representing the model class.
     * @param model       The Class object of the model.
     * @param columnName  The name of the column to filter by.
     * @param operator    The operator for comparison (e.g., '=', '>', '<').
     * @param value       The value to compare against.
     * @return            A Response object containing a list of matching records.
     */
    protected <T> Response<ArrayList<T>> findWhere(Class<T> model, String columnName, String operator, String value) {    
        Response<ArrayList<T>> response = new Response<>();
        try {
        	if (value == null || value.trim().isEmpty()) {
        	    throw new IllegalArgumentException("Search value cannot be null or empty.");
        	}
            ArrayList<T> records = new ArrayList<>();
            // Construct the SQL query with the specified condition
            String query = "SELECT * FROM " + getTableName() + " WHERE " + columnName + " " + operator + " ?";
            
            Singleton dbConnection = Singleton.getInstance();
            PreparedStatement ps = dbConnection.prepareStatement(query);
            
            ps.setString(1, value);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Create a new instance of T using reflection
                T instance = model.getDeclaredConstructor().newInstance();
                Field[] fields = instance.getClass().getDeclaredFields();
                
                for (Field field : fields) {
                    // Skip fields named "TABLE_NAME" and "PRIMARY_KEY"
                    if(field.getName().equalsIgnoreCase("TABLE_NAME") || field.getName().equalsIgnoreCase("PRIMARY_KEY")) {
                        continue;
                    }
                    // Make the field accessible for reflection
                    field.setAccessible(true);
                    // Retrieve the field's value from the current row in the result set
                    Object fieldValue = rs.getObject(field.getName());
                    // Assign the retrieved value to the field in the instance
                    field.set(instance, fieldValue);
                }
                records.add(instance);
            }
            
            response.setMessage("Success: Retrieved records based on the specified condition.");
            response.setSuccess(true);
            response.setData(records);
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            response.setMessage("Database Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }

   
    
    
}

