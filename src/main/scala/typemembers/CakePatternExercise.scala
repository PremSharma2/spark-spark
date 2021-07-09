package typemembers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object CakePatternExercise  extends App {
/*
TODO
   Layered component is the building block of the cake pattern.
    Consider following scenario.
   A system deals with employees.
    User can enter employees via command line.
     Then the system validates the entered employee and save it in the database.
     Finally employee will uploaded to cloud(via REST API).
     If we apply cake pattern to build this system,
      first we have to identifies the components.
       In here we can have two main components.
       Employee DB component
       (Which deals with get, insert, update, delete employee records via the database)
       Employee Service component(Which deals with POST employee details to cloud REST service)

   TODO here
       Actual database related functions comes with EmployeeDb trait.
        The functionality of the EmployeeDb wraps with EmployeeDbComp trait.
         We gives a single entry point to EmployeeDb trait from EmployeeDbComp trait

 */

  // TODO ist component

  /*
  TODO
       EmployeeDb component is an abstract component.
          We can have multiple implementations of this component. For an instance we can have
        Appache cassandra based EmployeeDb component
      MongoDB based EmployeeDb component
     Mocked EmployeeDb component for unit tests
       Following is the cassnadra based EmployeeDb component implementation.
   */
  class InvalidEmployeeInput(message:String) extends RuntimeException
case class Employee(empId:Int,name:String)
  //single entry point to DB this component is used for as single entry point to EmployeeDb trait
  trait EmployeeDbComponent {

    val employeeDb: EmployeeDb

    trait EmployeeDb {
      def createEmployee(employee: Employee)
      def getEmployee(empId: Int): Employee
    }

  }

  trait CassandraEmployeeDbComp extends EmployeeDbComponent {

   // self: CakezCassandraCluster =>

    val employeeDb = new CassandraEmployeeDb

    class CassandraEmployeeDb extends EmployeeDb {

      //def logger = LoggerFactory.getLogger(this.getClass)

      override def createEmployee(employee: Employee) = {
        //logger.debug(s"Create employee with id: ${employee.empId} name: ${employee.name}")

        // cassandra based insert query
        //val statement = QueryBuilder.insertInto("employee")
        //  .value("emp_id", employee.empId)
         // .value("name", employee.name)
        //session.execute(statement)
      }

      override def getEmployee(empId: Int): Employee = {
      //  logger.debug(s"get employee with ID: ${empId}")

        // cassandra based slect query
        //val selectStmt = select().all()
         // .from("employee")
         // .where(QueryBuilder.eq("emp_id", empId))
         // .limit(1)

        //val resultSet = session.execute(selectStmt)
       // val row = resultSet.one()

        //Employee(row.getInt("emp_id"), row.getString("name"), row.getString("department"))
        Employee(21,"")
      }
    }

  }

  /*
 TODO
      Employee Service Component
      Following is the layered design of the Employee Service component.
      Main functionality of this component is dealing
      with the could REST API in order to GET, PUT, POST, DELETE employees.
      We have adopted same approach i.e one single entry point
   */

  trait EmployeeServiceComp {

    val service: EmployeeService

    trait EmployeeService {
      def POST(employee: Employee): Future[Unit]
      def GET(id: Int): Future[Employee]
    }

  }

  trait SprayEmployeeServiceCompImpl extends EmployeeServiceComp  {

   // this: CakezActorSystem =>

    val employeeService = new SprayEmployeeService

    class SprayEmployeeService extends EmployeeService {

      //def logger = LoggerFactory.getLogger(this.getClass)

      override def POST(employee: Employee): Future[Unit] = {
       // import system.dispatcher
        //import com.pagero.cakez.protocols.EmployeeProtocol._

        //logger.debug(s"POST employee with id: ${employee.emp_id} name: ${employee.name}")

       // val pipeline = sendReceive
       // val response = pipeline {
        //  Post(s"http://$apiHost:$apiPort/api/v1/users/", employee)
        Future()
        }

        //response.map(_.entity.asInstanceOf[Unit])
      override def GET(id: Int): Future[Employee] = ???
    }

       def GET(id: Int): Future[Employee] = {
       // import system.dispatcher
        //import com.pagero.cakez.protocols.EmployeeProtocol._

        //logger.debug("GET employee with id: " + id)

       // val pipeline = sendReceive ~> unmarshal[Employee]
       // val response: Future[Employee] = pipeline {
        //  Get(s"http://$apiHost:$apiPort/api/v1/users/$id/?format=json")
        Future[Employee](Employee(21,"prem"))
        }

        //response
      }

/*
TODO
       EmployeeHandler
       In previous section we have discussed about the components(layered components) of our system.
       We have setup two components. Now we need to integrate these components in order to achieve the system functionality.
       Assume there is a handler class(EmployeeHandler) which directly takes user inputs,
       validates them, and manages employee creation(in database) and uploading.
       In order to creates and upload employees,
        handler class needs to have a dependencies to EmployeeDbComp and EmployeeServiceComp.
         Following is the EmployeeHandler implementation
 */


  class EmployeeHandler {

    this: EmployeeDbComponent with EmployeeServiceComp =>

    //def logger = LoggerFactory.getLogger(this.getClass)

    def createEmployee(inputEmp: String): Employee = {
      // inputEmp comes as 'emp_id name department'
      val tokens = inputEmp.split(" ")

      // validate input content
      if (tokens.length != 3) {
     //   logger.error(s"Invalid input: ${inputEmp}, employee should contains [id name department]")
       // throw InvalidEmployeeInput("Invalid input, employee should contains [id name department]")
      }

      // validate emp_id
      if (!Try(tokens(0).toInt).isSuccess) {
       // logger.error(s"Invalid employee ID: ${tokens(0)}")
       // throw InvalidEmployeeId("Invalid employee ID " + tokens(0))
      }

      val employee = Employee(tokens(0).toInt, tokens(1))

      // create employee via db
      this.employeeDb.createEmployee(employee)

      // POST employee to cloud
      this.service.POST(employee)

      employee
    }

    def findEmployee(empId: Int): Employee = {
      // validate empId
      if (empId == 0) {
      // throw InvalidEmployeeId("Invalid employee ID " + empId)

      // find employee via db
      employeeDb.getEmployee(empId)

      }
      Employee(22,"")
    }

  }

/*
TODO
     We have injected  dependencies to EmployeeHandler via self typed annotations.
    this: EmployeeDbComp with EmployeeServiceComp =>
    When instantiating the employee handler we have to provide EmployeeDbComp and EmployeeServiceComp.
     Following is the way to do that.
    val employeeHandler = new EmployeeHandler with CassandramEployeeDbComp with SprayEmployeeServiceComp
 */
  }


