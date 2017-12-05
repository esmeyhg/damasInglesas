package Entidades;

import Entidades.Puntajes;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-28T14:24:54")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, Integer> idUsuario;
    public static volatile SingularAttribute<Usuario, String> passwordUsuario;
    public static volatile ListAttribute<Usuario, Puntajes> puntajesList;
    public static volatile SingularAttribute<Usuario, String> nombreUsuario;

}