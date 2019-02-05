package chushka.repository;

import chushka.domain.entities.Product;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    private EntityManager entityManager;

    public ProductRepositoryImpl() {
        this.entityManager = Persistence.createEntityManagerFactory("chushka").createEntityManager();
    }

    @Override
    public Product save(Product entity) {
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(entity);
        this.entityManager.getTransaction().commit();
        return entity;
    }

    @Override
    public Product findById(String id) {
        Product product = this.entityManager.find(Product.class, id);
        return product;
    }

    @Override
    public List<Product> findAll() {
        this.entityManager.getTransaction().begin();
        CriteriaQuery<Product> query = this.entityManager.getCriteriaBuilder().createQuery(Product.class);
        query.from(Product.class);
        this.entityManager.getTransaction().commit();
        List<Product> products = this.entityManager.createQuery(query).getResultList();
        return products;
    }

    @Override
    public Product findByName(String name) {
        this.entityManager.getTransaction().begin();
        Product product = this.entityManager
                .createQuery("SELECT p FROM products p WHERE p.name = :name", Product.class)
                .setParameter("name", name)
                .getSingleResult();
        this.entityManager.getTransaction().commit();
        return product;
    }
}
