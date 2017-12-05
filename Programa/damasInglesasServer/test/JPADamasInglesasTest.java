import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author yamii
 */
public class JPADamasInglesasTest {
  private static EntityManagerFactory entityManagerFactory;
  private static EntityManager entityManager;
  private static controladores.UsuarioJpaController usuario;
  
  public JPADamasInglesasTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }
  
  /*@Test
  public void testGetUsuarios () throws RemoteException {
    List<Logica.Cuenta> usuarios = usuario.getUsuarios();
    assertEquals(usuarios.size(),2,0);
  }*/

 @Test
 public void testRegistrarUsuario() {
   logica.Cuenta cuenta = null;
   String nombre = "Esmeralda";
   String contrasena = "1234m";
        
   controladores.UsuarioJpaController controladorUsuario = new controladores.UsuarioJpaController();

   entidades.Usuario usuario1 = new entidades.Usuario();
   usuario1.setNombreUsuario(nombre);
   usuario1.setPasswordUsuario(contrasena);
   controladorUsuario.create(usuario1); 
   Assert.assertNotNull("Ya existe usuario");
 }
}
