/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domaines;

import entities.Employee;
import entities.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.ChartSeries;
import service.EmployeeService;
import service.ServiceService;

/**
 *
 * @author hp
 */


@ManagedBean(name="employeBean")
public class EmployeBean {
    private Employee employe ;
    private EmployeeService employeeService;
    private Employee chef;   
    private Service service;
    private static ChartModel barModel;
    private ServiceService serviceService; 
    private Employee selectedChef;
     private List<Employee> employes;

    public Employee getSelectedChef() {
        return selectedChef;
    }

    public void setSelectedChef(Employee selectedChef) {
        this.selectedChef = selectedChef;
    }

    public EmployeBean() {
        employe=new Employee();
        employe = new Employee();
        service = new Service();
        chef = new Employee();
        employe.setChef(chef);
        employe.setService(service);
        employeeService=new EmployeeService();
       serviceService=new ServiceService();
    }
 public String onCreateAction() {
        if (employe.getChef() != null) {
            Employee chef = employeeService.getById(employe.getChef().getId());
           
            employe.setChef(chef);
        } else {
            employe.setChef(null);
        }
        chef.setChef(null);
       employeeService.create(employe);
        employe = new Employee();
        service = new Service();
        chef = new Employee();
        employe.setChef(null);
        employe.setService(null);
        return null;
    }
    public List<Employee> getEmployes() {
        if(employes == null){
            employes=employeeService.getAll();
        }
        return employes;
    }

    public void setEmployes(List<Employee> employes) {
        this.employes = employes;
    }
    
    public  String onDeleteAction(){
        employe.setService(null);
        employeeService.delete(employe);
        return null;
    }
    public void onCancel(RowEditEvent event) {
    }
    public void onEdit(RowEditEvent event) {
        try  {
        employe = (Employee) event.getObject();
        Service s = serviceService.getById(this.employe.getService().getId());
    
        employe.setService(s);
        employe.getService().setCode(s.getCode());
        employeeService.update(employe);
    } catch (Exception e) {
       
            e.printStackTrace();
    }
        
    }

  

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Employee getEmploye() {
        return employe;
    }

    public void setEmploye(Employee employe) {
        this.employe = employe;
    }
     public static ChartModel getBarModel() {
        return barModel;
    }
     public ChartModel initBarModel() {
        CartesianChartModel model = new CartesianChartModel();
        ChartSeries emp = new ChartSeries();
        emp.setLabel("employe by services");
        model.setAnimate(true);
        for (Object[] m :employeeService.nbremployebyservice()) {
            System.out.println(m[1]);
            emp.set(m[1], Integer.parseInt(m[0].toString()));
        }
        model.addSeries(emp);

        return model;
    }
       public void handleFileUpload(FileUploadEvent event) {
        try {
            InputStream inputStream = event.getFile().getInputstream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] fileContent = outputStream.toByteArray();
            System.out.println("fileContent: " + Arrays.toString(fileContent));
            employe.setPhoto(fileContent);
            outputStream.close();
            inputStream.close();

            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error uploading file");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
  
    public ByteArrayInputStream getImageStream(byte[] imageBytes) {
        if (null != imageBytes) {
            ByteArrayInputStream b = new ByteArrayInputStream(imageBytes);
            int byteRead;
            while ((byteRead = b.read()) != -1) {
                System.out.print((char) byteRead);
            }
            return b;
        } else {
            return null;
        }

    }
    
    public String getImageBase64(byte[] imageBytes) {
    if (imageBytes != null && imageBytes.length > 0) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    return "";
}
     
    
    
}