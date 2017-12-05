package logica;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class MovimientoFichas {
  private TipoMovimiento tipoMov;
  private Ficha ficha;
  
  public MovimientoFichas (TipoMovimiento tipoMov, Ficha ficha) {
    this.tipoMov = tipoMov;
    this.ficha = ficha;
  }
  
  public MovimientoFichas (TipoMovimiento tipoMov) {
    this(tipoMov, null);
  }
  
  public TipoMovimiento getMovimiento () {
    return tipoMov;
  }
  
  public Ficha getFicha () {
    return ficha;
  }
}
