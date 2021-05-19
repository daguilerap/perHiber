import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        // Se crea y guarda en bd un evento
        Long idEvent = createAndStoreEvent("El Event", new Date());

        // Se crea y guarda en bd una persona
        Long idPerson = createAndStorePerson("Juan", "Cortés", 34);

        // Se asocian en bd persona y evento
        addPersonToEvent(idPerson, idEvent);

        // Se añade un nuevo evento a la persona
        addNewEventToPerson("Nuevo evento", new Date(), idPerson);

        // Se listan las personas con sus eventos
        listPersons();
    }

    private static void addPersonToEvent(Long personId, Long eventId)
    {
        // Sesion
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        // Se cargan perona y evento con su id.
        Person aPerson = (Person) session.load(Person.class, personId);
        Event anEvent = (Event) session.load(Event.class, eventId);

        // Se añade el evento a la persona usando métodos normales del
        // java bean Person
        aPerson.getEvents().add(anEvent);

        // Se termina la transaccion
        session.getTransaction().commit();
    }

    private static void addNewEventToPerson(String eventName, Date eventDate, Long personId)
    {
        // Sesion
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        // Se carga la persona de bd
        Person aPerson = (Person) session.load(Person.class, personId);

        // Se crea un evento y se salva
        Event e = new Event();
        e.setTitle(eventName);
        e.setDate(eventDate);
        session.save(e);

        // Se añade el evento a la persona y se salva
        aPerson.getEvents().add(e);
        session.save(aPerson);

        // fin de sesion
        session.getTransaction().commit();
    }

    private static Long createAndStorePerson(String nombre, String apellido, int edad)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Person thePerson = new Person();
        thePerson.setFirstname(nombre);
        thePerson.setLastname(apellido);
        thePerson.setAge(edad);
        session.save(thePerson);
        session.getTransaction().commit();
        System.out.println("Insertado: "+thePerson);
        return thePerson.getId();
    }

    private static Long createAndStoreEvent(String title, Date theDate)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        session.save(theEvent);
        session.getTransaction().commit();
        System.out.println("Insertado: "+theEvent);
        return theEvent.getId();
    }

    private static List<Person> listPersons()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Person> result = (List<Person>)session.createQuery("from Person").list();
        for (Person persona : result) {
            System.out.println("Leido: "+ persona);
        }
        return result;
    }
}