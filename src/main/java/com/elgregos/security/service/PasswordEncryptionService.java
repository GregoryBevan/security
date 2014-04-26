package com.elgregos.security.service;

import java.io.IOException;
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

import com.elgregos.security.entities.User;

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
	 * @param user
	 *            Utilisateur
	 * @param inputPassword
	 *            Mot de passe entré
	 * @throws SecurityException
	 */
	public void setEncryptedPassword(final User user, final String inputPassword)
			throws SecurityException {
		byte[] salt = this.generateSalt();
		final byte[] encryptedPassword = this.getEncryptedPassword(
				inputPassword, salt);
		user.setPassword(this.byteToBase64(encryptedPassword));
		user.setSalt(this.byteToBase64(salt));
	}

	/**
	 * Retourne le du mot de passe crypté
	 * 
	 * @param password
	 *            Mot de passe
	 * @param salt
	 *            Donnée aléatoire
	 * @return Hachage du mot de passe entré
	 * @throws SecurityException
	 */
	private byte[] getEncryptedPassword(String password, byte[] salt)
			throws SecurityException {
		try {
			String algorithm = "PBKDF2WithHmacSHA1";
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 20000,
					762);
			SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
			return f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
			throw new SecurityException(exception.getMessage(),
					exception.getCause());
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
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// Salt generation 64 bits long
			byte[] bSalt = new byte[8];
			random.nextBytes(bSalt);
			return bSalt;
		} catch (NoSuchAlgorithmException nsae) {
			throw new SecurityException(nsae.getMessage(), nsae.getCause());
		}
	}

	/**
	 * Retourne un tableau d'octet à partir d'une représentation base 64
	 * 
	 * @param base64String
	 *            Représentation base 64
	 * @return byte[]
	 * @throws IOException
	 */
	private byte[] base64ToByte(String base64String) throws IOException {
		Decoder decoder = Base64.getDecoder();
		return decoder.decode(base64String);
	}

	/**
	 * Retourne une représentation base 64 d'un tableau d'octets
	 * 
	 * @param byteArray
	 *            Tableau d'octet à encoder en base 64
	 * @return Représentation base 64
	 * @throws IOException
	 */
	private String byteToBase64(byte[] byteArray) {
		Encoder endecoder = Base64.getEncoder();
		return endecoder.encodeToString(byteArray);
	}

}
