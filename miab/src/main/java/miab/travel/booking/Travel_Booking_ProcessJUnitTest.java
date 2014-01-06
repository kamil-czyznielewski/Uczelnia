package miab.travel.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import miab.travel.booking.pojos.CreditCard;
import miab.travel.booking.pojos.Variable;

import org.jbpm.process.instance.event.DefaultSignalManagerFactory;
import org.jbpm.process.instance.impl.DefaultProcessInstanceManagerFactory;
import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.io.ResourceType;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieRuntime;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.task.api.UserGroupCallback;

public class Travel_Booking_ProcessJUnitTest extends JbpmJUnitBaseTestCase {

	private RuntimeManager manager;
	private RuntimeEngine engine;
	private KieSession ksession;
	
	@Before
	@SuppressWarnings("restriction")
	public void setUpTest() {
		Map<String, ResourceType> resources = new HashMap<String, ResourceType>();
		for (String p : new String[]{"travel.bpmn", "simple.bpmn"}) {
           resources.put(p, ResourceType.BPMN2);
        }
		RuntimeEnvironmentBuilder builder = null;
        if (!setupDataSource){
            builder = RuntimeEnvironmentBuilder.getEmpty()
            .addConfiguration("drools.processSignalManagerFactory", DefaultSignalManagerFactory.class.getName())
            .addConfiguration("drools.processInstanceManagerFactory", DefaultProcessInstanceManagerFactory.class.getName());
        } else if (sessionPersistence) {
            builder = RuntimeEnvironmentBuilder.getDefault().entityManagerFactory(getEmf());
        } else {
            builder = RuntimeEnvironmentBuilder.getDefaultInMemory();       
        }
        Properties properties= new Properties();
        properties.setProperty("Piotr", "Manager");
        properties.setProperty("Kamil", "Employee");
        properties.setProperty("Zenon", "");
        UserGroupCallback userGroupCallback = new JBossUserGroupCallbackImpl(properties);
        builder.userGroupCallback(userGroupCallback);
        
        for (Map.Entry<String, ResourceType> entry : resources.entrySet()) {            
            builder.addAsset(ResourceFactory.newClassPathResource(entry.getKey()), entry.getValue());
        }
//		RuntimeManager manager = createRuntimeManager(new String[]{"travel.bpmn", "simple.bpmn"});
		this.manager = createRuntimeManager(Strategy.SINGLETON, resources, builder.get(), null);
		this.engine = getRuntimeEngine(null);
		this.ksession = engine.getKieSession();
	}
	
	@After
	public void disposeTest() {
        this.manager.disposeRuntimeEngine(this.engine);
        this.manager.close();
	}
	
	
	
	@Test
	@SuppressWarnings("restriction")
    public void testProcess() throws Exception {
		this.ksession.getWorkItemManager().registerWorkItemHandler("Service Task", new org.jbpm.bpmn2.handler.ServiceTaskHandler());
		TaskService taskService = this.engine.getTaskService();
		
		this.ksession.addEventListener(new ProcessEventListener() {
			
			@Override
			public void beforeVariableChanged(ProcessVariableChangedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeProcessStarted(ProcessStartedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeProcessCompleted(ProcessCompletedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeNodeTriggered(ProcessNodeTriggeredEvent arg0) {
				System.out.println("\t NODE TRIGGERED : " + arg0.getNodeInstance().getNodeName());
			}
			
			@Override
			public void beforeNodeLeft(ProcessNodeLeftEvent arg0) {
				System.out.println("\t NODE LEFT : " + arg0.getNodeInstance().getNodeName());
				
			}
			
			@Override
			public void afterVariableChanged(ProcessVariableChangedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterProcessStarted(ProcessStartedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterProcessCompleted(ProcessCompletedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterNodeTriggered(ProcessNodeTriggeredEvent arg0) {
				
			}
			
			@Override
			public void afterNodeLeft(ProcessNodeLeftEvent arg0) {
				
			}
		});
		
		Map<String, Object> params = new HashMap<String, Object>();
        // initialize variables here if necessary
			Variable v = new Variable();
	        v.vars.put("credit_card", new CreditCard("Czyżnielewski Kamil", 1230, "MasterCard"));
        params.put("variable", v);
        ProcessInstance processInstance = this.ksession.startProcess("miab.travel.booking", params);
        
        assertNodeTriggered(processInstance.getId(), "Request Task");
        List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner("Kamil", "en-UK");
		assertEquals(1, tasks.size());
		TaskSummary task = tasks.get(0);
		System.out.println("'Kamil' completing task " + task.getName() + ": " + task.getDescription());
		taskService.start(task.getId(), "Kamil");
		Map<String, Object> results = new HashMap<String, Object>();
		taskService.complete(task.getId(), "Kamil", results);

        assertNodeTriggered(processInstance.getId(), "Approval Task");
//        Thread.sleep(5000);
        tasks = taskService.getTasksAssignedAsPotentialOwner("Piotr", "en-UK");
		assertEquals(1, tasks.size());
		task = tasks.get(0);
		System.out.println("'Piotr' completing task " + task.getName() + ": " + task.getDescription());
		taskService.start(task.getId(), "Piotr");
		results = new HashMap<String, Object>();
		taskService.complete(task.getId(), "Piotr", results);
		
        
        assertProcessInstanceCompleted(processInstance.getId(), this.ksession);
    }
	
	public Travel_Booking_ProcessJUnitTest() {
		super(true, true);
	}

}