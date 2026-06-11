package com.uff.gestaoclinicaveterinaria.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes das regras de segurança da gestão de usuários pelo ADMIN.
 * Cobrem os guardrails críticos sem precisar de banco.
 */
class UsuarioPolicyTest {

    @Test
    @DisplayName("Papéis válidos são aceitos e inválidos rejeitados")
    void validacaoDePapel() {
        assertTrue(UsuarioPolicy.papelValido("TUTOR"));
        assertTrue(UsuarioPolicy.papelValido("VETERINARIO"));
        assertTrue(UsuarioPolicy.papelValido("ADMIN"));

        assertFalse(UsuarioPolicy.papelValido("ROOT"));
        assertFalse(UsuarioPolicy.papelValido("admin")); // case-sensitive
        assertFalse(UsuarioPolicy.papelValido(""));
        assertFalse(UsuarioPolicy.papelValido(null));
    }

    @Test
    @DisplayName("Admin não pode excluir a própria conta")
    void naoPermiteAutoExclusao() {
        Long mesmoId = 1L;
        assertFalse(UsuarioPolicy.podeExcluir(mesmoId, mesmoId, "ADMIN", 5));
    }

    @Test
    @DisplayName("Não permite excluir o último administrador")
    void naoPermiteExcluirUltimoAdmin() {
        assertFalse(UsuarioPolicy.podeExcluir(2L, 1L, "ADMIN", 1));
    }

    @Test
    @DisplayName("Permite excluir um admin quando existe mais de um")
    void permiteExcluirAdminQuandoHaOutros() {
        assertTrue(UsuarioPolicy.podeExcluir(2L, 1L, "ADMIN", 2));
    }

    @Test
    @DisplayName("Permite excluir tutor/veterinário normalmente")
    void permiteExcluirNaoAdmin() {
        assertTrue(UsuarioPolicy.podeExcluir(3L, 1L, "TUTOR", 1));
        assertTrue(UsuarioPolicy.podeExcluir(4L, 1L, "VETERINARIO", 1));
    }

    @Test
    @DisplayName("Não permite rebaixar o último administrador")
    void naoPermiteRebaixarUltimoAdmin() {
        assertFalse(UsuarioPolicy.podeAlterarPapel("ADMIN", "TUTOR", 1));
    }

    @Test
    @DisplayName("Permite rebaixar admin quando existe mais de um")
    void permiteRebaixarAdminQuandoHaOutros() {
        assertTrue(UsuarioPolicy.podeAlterarPapel("ADMIN", "VETERINARIO", 2));
    }

    @Test
    @DisplayName("Promover para admin é sempre permitido")
    void permitePromoverParaAdmin() {
        assertTrue(UsuarioPolicy.podeAlterarPapel("TUTOR", "ADMIN", 1));
    }

    @Test
    @DisplayName("Papel novo inválido é rejeitado na alteração")
    void rejeitaPapelInvalidoNaAlteracao() {
        assertFalse(UsuarioPolicy.podeAlterarPapel("TUTOR", "ROOT", 3));
    }
}
