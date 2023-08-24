package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }
   @Override
   public User getUser(String model, int series){
      //Выбираем пользователей из таблицы Юзер, загружаем связный с ним объект и пишем для него условие
      //параметры в запросе обозночаем через знак :
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(
              "select user from User user join fetch user.car car where car.model = :model and car.series = :series",
              User.class
      );
      //передаем значения для параметров
      query.setParameter("model", model);
      query.setParameter("series", series);
      //Берем только первого попавшегося под условие или возращаем null если таких не было
      return query.getResultList().stream().findFirst().orElse(null);
   }

}
