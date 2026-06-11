package com.uff.gestaoclinicaveterinaria.util;

import java.util.Set;

/**
 * Regras de segurança (puras, sem dependência de banco) para a gestão de usuários
 * feita pelo ADMIN. Centralizadas aqui para serem testáveis isoladamente pelo QA e
 * reutilizadas pelo {@code UsuarioServlet}.
 */
public final class UsuarioPolicy {

    public static final Set<String> PAPEIS_VALIDOS = Set.of("TUTOR", "VETERINARIO", "ADMIN");

    private UsuarioPolicy() {
    }

    /** O papel precisa ser exatamente um dos valores aceitos pelo CHECK do banco. */
    public static boolean papelValido(String role) {
        return role != null && PAPEIS_VALIDOS.contains(role);
    }

    /**
     * Admin não pode excluir a própria conta (evita ficar sem acesso) nem remover o
     * último administrador do sistema (sempre deve existir ao menos um).
     */
    public static boolean podeExcluir(Long alvoId, Long logadoId, String roleAlvo, long totalAdmins) {
        if (alvoId == null) {
            return false;
        }
        if (alvoId.equals(logadoId)) {
            return false;
        }
        if ("ADMIN".equals(roleAlvo) && totalAdmins <= 1) {
            return false;
        }
        return true;
    }

    /**
     * Rebaixar (tirar o papel ADMIN de) o último administrador deixaria o sistema sem
     * nenhum admin — bloqueado.
     */
    public static boolean podeAlterarPapel(String roleAtual, String novoRole, long totalAdmins) {
        if (!papelValido(novoRole)) {
            return false;
        }
        boolean estaRebaixandoAdmin = "ADMIN".equals(roleAtual) && !"ADMIN".equals(novoRole);
        if (estaRebaixandoAdmin && totalAdmins <= 1) {
            return false;
        }
        return true;
    }
}
