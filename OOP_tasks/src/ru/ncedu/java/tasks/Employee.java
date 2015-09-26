package ru.ncedu.java.tasks;

/**
 * ЦЕЛЬ ЗАДАЧИ - разобраться с основами объектно-ориентированного программирования в Java,
 * принципами написания класса, реализации методов get/set, обращения к полям и методам объектов.
 * 
 * ЗАДАНИЕ
 * Реализовать класс, представляющий собой описание сотрудника компании (Employee).
 * 
 * ТРЕБОВАНИЯ
 * Экземпляр класса, реализующего данный интерфейс, должен соответствовать одному сотруднику.
 * Необходимо реализовать методы get/set для имени (и фамилии), полного имени, зарплаты сотрудника,
 *  а также его непосредственного менеджера и топового (самого вышестоящего) менеджера.
 * Данные (поля) должны быть объявлены как private-переменные класса. 
 * Методы get** / set** должны оперировать с этими полями.
 * Среди конструкторов должен быть конструктор без параметров (default constructor), причем public.
 * Класс должен работать корректно после вызова такого конструктора и без вызова set-метода(-ов).
 * По умолчанию (у только что созданного сотрудника) зарплата должна быть равна 1000.
 * 
 * ПРИМЕЧАНИЯ
 * Задачу можно решать без явной обработки и генерации исключительных ситуаций (Exceptions).
 * Вот как должна выглядеть реализация данного интерфейса:
 * public class public class EmployeeImpl implements Employee {  }
 * 
 * @author Alexander Kharichkin
 * @author Yuriy Popov
 */
public interface Employee {
	/**
	 * @return Зарплата сотрудника на настоящий момент.
	 */
	int getSalary();

	/**
	 * Увеличивает зарплату сотрудника на заданное значение
	 * @param value Значение, на которое нужно увеличить
	 */
	public void increaseSalary(int value);

	/**
	 * @return Имя сотрудника
	 */
	public String getFirstName();

	/**
	 * Устанавливает имя сотрудника
	 * @param firstName Новое имя
	 */
	public void setFirstName(String firstName);

	/**
	 * @return Фамилия сотрудника
	 */
	public String getLastName();

	/**
	 * Устанавливает фамилию сотрудника
	 * @param lastName Новая фамилия
	 */
	public void setLastName(String lastName);

	/**
	 * @return Имя и фамилия сотрудника, разделенные символом " " (пробел)
	 */
	public String getFullName();

	/**
	 * Устанавливает Менеджера сотрудника.
	 * @param manager Сотрудник, являющийся менеджером данного сотрудника. 
	 * НЕ следует предполагать, что менеджер является экземпляром класса EmployeeImpl.
	 */
	public void setManager(Employee manager);

	/**
	 * @return Имя и фамилия Менеджера, разделенные символом " " (пробел).
	 * Если Менеджер не задан, возвращает строку "No manager".
	 */
	public String getManagerName();

	/**
	 * Возвращает Менеджера верхнего уровня, т.е. вершину иерархии сотрудников, 
	 *   в которую входит данный сотрудник.
	 * Если над данным сотрудником нет ни одного менеджера, возвращает данного сотрудника.
	 * Замечание: поскольку менеджер, установленный методом {@link #setManager(Employee)},
	 *   может быть экзепляром другого класса, при поиске топ-менеджера нельзя обращаться
	 *   к полю класса EmployeeImpl. Более того, поскольку в интерфейсе Employee не объявлено
	 *   метода getManager(), поиск топ-менеджера невозможно организовать в виде цикла.
	 *   Вместо этого нужно использовать рекурсию (и это "более объектно-ориентированно").
	 */
	public Employee getTopManager();
}