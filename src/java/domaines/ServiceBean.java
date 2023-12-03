/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domaines;

import entities.Service;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.RowEditEvent;
import service.ServiceService;


@ManagedBean
@RequestScoped
public class ServiceBean {
    private Service service;
    private List<Service> services;
    
    private ServiceService  ss;
 
    public ServiceBean() {
        service = new Service();
        ss = new ServiceService();
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
    
    public void onCreateAction(){
        ss.create(service);
        service = new Service();
    }

    public List<Service> getServices() {
        if(services == null)
            services = ss.getAll();
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
    public String onDeleteAction() {
        ss.delete(ss.getById(service.getId()));
        return null;
    }
     public void onEdit(RowEditEvent event) {
        service = (Service) event.getObject();
        Service service = ss.getById(this.service.getId());
        service.setLibelle(service.getLibelle());
        service.setCode(service.getCode());
        ss.update(service);
       
    }

    public void onCancel(RowEditEvent event) {
    }
    
}
