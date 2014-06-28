package com.elgregos.security.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.Singleton;

import com.elgregos.security.data.entities.User;

/**
 * Description : Service de cryptage du mot de passe
 *
 * @author Grégo
 * @date 10 mai 2013
 */
@Singleton
public class PasswordEncryptionService {

	/**
	 * Définit le mot de passe encrypté de l'utilisateur
	 *
	 * @param user Utilisateur
	 * @param inputPassword Mot de passe entré
	 * @throws SecurityException
	 */
	public void setEncryptedPassword(final User user) throws SecurityException {
		final byte[] salt = generateSalt();
		final byte[] encryptedPassword = getEncryptedPassword(user.getPassword(), salt);
		user.setPassword(byteToBase64(encryptedPassword));
		user.setSalt(byteToBase64(salt));
	}

	/**
	 * Retourne le mot de passe crypté
	 *
	 * @param password Mot de passe
	 * @param salt Donnée aléatoire
	 * @return Mot de passe crypté
	 */
	public String getEncryptedPassword(final String password, final String salt) {
		final byte[] saltAsByte = base64ToByte(salt);
		return byteToBase64(getEncryptedPassword(password, saltAsByte));
	}

	/**
	 * Retourne le tableau d'octet du mot de passe crypté
	 *
	 * @param password Mot de passe
	 * @param salt Donnée aléatoire
	 * @return Hachage du mot de passe entré
	 * @throws SecurityException
	 */
	private byte[] getEncryptedPassword(final String password, final byte[] salt) throws SecurityException {
		try {
			final String algorithm = "PBKDF2WithHmacSHA1";
			final KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 20000, 762);
			final SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
			return f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
			throw new SecurityException(exception.getMessage(), exception.getCause());
		}
	}

	/**
	 * Génère une donnée aléatoire pour sécuriser l'encryption
	 *
	 * @return Salt
	 * @throws SecurityException
	 */
	private byte[] generateSalt() throws SecurityException {
		try {
			final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// Salt generation 64 bits long
			final byte[] bSalt = new byte[8];
			random.nextBytes(bSalt);
			return bSalt;
		} catch (final NoSuchAlgorithmException nsae) {
			throw new SecurityException(nsae.getMessage(), nsae.getCause());
		}
	}

	/**
	 * Retourne un tableau d'octet à partir d'une représentation base 64
	 *
	 * @param base64String Représentation base 64
	 * @return byte[]
	 */
	private byte[] base64ToByte(final String base64String) {
		final Decoder decoder = Base64.getDecoder();
		return decoder.decode(base64String);
	}

	/**
	 * Retourne une représentation base 64 d'un tableau d'octets
	 *
	 * @param byteArray Tableau d'octet à encoder en base 64
	 * @return Représentation base 64
	 */
	private String byteToBase64(final byte[] byteArray) {
		final Encoder endecoder = Base64.getEncoder();
		return endecoder.encodeToString(byteArray);
	}

}
