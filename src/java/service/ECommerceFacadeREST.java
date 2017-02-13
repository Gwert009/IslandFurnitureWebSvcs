package service;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("commerce")
public class ECommerceFacadeREST {

    @Context
    private UriInfo context;

    public ECommerceFacadeREST() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    
    @PUT
    @Path("createECommerceTransactionRecord")
    @Produces("application/json")
    public Response createECommerceTransactionRecord(@QueryParam("memberID") Long memberID, @QueryParam("amountPaid") Double amountPaid, @QueryParam("countryID") Long countryID) {
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");
            String stmt = "INSERT INTO salesrecordentity (AMOUNTDUE, AMOUNTPAID, AMOUNTPAIDUSINGPOINTS, CREATEDDATE, CURRENCY, LOYALTYPOINTSDEDUCTED, POSNAME, RECEIPTNO, SERVEDBYSTAFF, MEMBER_ID, STORE_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, amountPaid);
            ps.setDouble(2, amountPaid);
            ps.setDouble(3, 0);
            //ps.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            
            int ctry = countryID.intValue();
            switch(ctry) {
                case 25: 
                    ps.setString(5, "SGD");
                    break;
                default:
                    ps.setString(5, "USD");
                    
            }
            
            ps.setInt(6, 0);
            ps.setString(7, "Counter 1");
            ps.setString(8, "ECommerce");
            ps.setString(9, "Cashier 1");
            ps.setInt(10, 1);
            ps.setInt(11, 10001);
            
            
            ps.executeUpdate();
            
            String key = "";
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                System.out.println("Key returned from getGeneratedKeys():"
                        + rs.getInt(1));
                key = ""+rs.getInt(1);
                
            } 
            rs.close();
            
            GenericEntity<String> entity = new GenericEntity<String>(key) {
            };
            conn.close();
            return Response.status(201).entity(entity).build();
            
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        
    }
    
    @PUT
    @Path("createECommerceLineItemRecord")
    @Produces("application/json")
    public Response createECommerceLineItemRecord(@QueryParam("salesRecordID") Long salesRecordID, @QueryParam("id") String id) {
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");
            String stmt = "INSERT INTO salesrecordentity_lineitementity (SalesRecordEntity_ID, itemsPurchased_ID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setInt(1, Integer.parseInt(salesRecordID.toString()));
            ps.setInt(2, Integer.parseInt(id));
            
            ps.executeUpdate();
            
            GenericEntity<String> entity = new GenericEntity<String>("1") {
            };
            conn.close();
            return Response.status(201).entity(entity).build();
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        
    }
    
    
}
