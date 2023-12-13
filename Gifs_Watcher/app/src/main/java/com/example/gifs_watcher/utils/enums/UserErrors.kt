package com.example.gifs_watcher.utils.enums

enum class UserErrors (val message : String) {
    ID_OR_PASSWORD_INVALID("L'identifiant ou le mot de passe saisi n'est pas valide."),
    ID_EMPTY("L'identifiant saisi est vide."),
    PASSWORD_INVALID("Le mot de passe saisi n'est pas valide."),
    PASSWORD_TOO_SHORT("Le mot de passe saisi est trop court (minimum 6 caractères)."),
    PASSWORD_IS_EMPTY("Le mot de passe saisi est vide."),
    USERNAME_ALREADY_USED("Le nom d'utilisateur saisi est déjà utilisé."),
    USERNAME_TOO_SHORT("Le nom d'utilisateur saisi est trop court (minimum 3 caractères)."),
    USERNAME_TOO_LONG("Le nom d'utilisateur saisi est trop long (maximum 20 caractères)."),
    USERNAME_IS_EMPTY("Le nom d'utilisateur saisi est vide."),
    USERNAME_CONTAINS_EXCEPTED_CHARACTERS("Le nom d'utilisateur saisi contient des caractères non autorisés."),
    EMAIL_ALREADY_USED("L'adresse mail saisie est déjà utilisée."),
    EMAIL_CONTAINS_EXCEPTED_CHARACTERS("L'adresse mail saisie contient des caractères non autorisés."),
    EMAIL_IS_EMPTY("L'adresse mail saisie est vide."),
    PASSWORDS_NOT_MATCHING("Les mots de passe saisis ne correspondent pas."),
    EMAIL_NOT_VALID("L'adresse mail saisie n'est pas valide."),
    USERNAME_NOT_VALID("Le nom d'utilisateur saisi n'est pas valide."),
    BIRTHDATE_NOT_VALID("La date de naissance saisie n'est pas valide."),
    BIRTHDATE_TOO_YOUNG("Vous devez avoir au moins 15 ans pour vous inscrire."),
    BIRTHDATE_IS_EMPTY("La date de naissance saisie est vide."),
    UNKNOWN_ERROR("Une erreur inconnue est survenue.")
}