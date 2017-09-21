-- MySQL Script generated by MySQL Workbench
-- 09/20/17 20:32:27
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema damasInglesas
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema damasInglesas
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `damasInglesas` DEFAULT CHARACTER SET utf8 ;
USE `damasInglesas` ;

-- -----------------------------------------------------
-- Table `damasInglesas`.`Puntajes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `damasInglesas`.`Puntajes` (
  `idPuntajes` INT NOT NULL AUTO_INCREMENT,
  `puntajes` INT NOT NULL,
  PRIMARY KEY (`idPuntajes`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `damasInglesas`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `damasInglesas`.`Usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nombreUsuario` VARCHAR(45) NOT NULL,
  `passwordUsuario` VARCHAR(45) NOT NULL,
  `Puntajes_idPuntajes` INT NOT NULL,
  PRIMARY KEY (`idUsuario`, `Puntajes_idPuntajes`),
  INDEX `fk_Usuario_Puntajes_idx` (`Puntajes_idPuntajes` ASC),
  CONSTRAINT `fk_Usuario_Puntajes`
    FOREIGN KEY (`Puntajes_idPuntajes`)
    REFERENCES `damasInglesas`.`Puntajes` (`idPuntajes`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
