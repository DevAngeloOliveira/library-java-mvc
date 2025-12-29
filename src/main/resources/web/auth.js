// Configuração da API
const API_URL = 'http://localhost:8080';

// Fazer login
async function fazerLogin() {
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    const erroDiv = document.getElementById('erro-login');
    const erroMensagem = document.getElementById('erro-mensagem');

    try {
        const response = await fetch(`${API_URL}/api/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        const result = await response.json();

        if (result.sucesso) {
            // Salvar token e dados do usuário
            localStorage.setItem('token', result.dados.token);
            localStorage.setItem('usuario', JSON.stringify(result.dados.usuario));
            
            // Redirecionar para a página principal
            window.location.href = 'index.html';
        } else {
            mostrarErro(result.mensagem || 'Erro ao fazer login');
        }
    } catch (error) {
        console.error('Erro ao fazer login:', error);
        mostrarErro('Servidor offline. Redirecionando para modo sem autenticação...');
        
        // Após 2 segundos, redireciona para index.html sem autenticação
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
    }
}

// Mostrar erro
function mostrarErro(mensagem) {
    const erroDiv = document.getElementById('erro-login');
    const erroMensagem = document.getElementById('erro-mensagem');
    
    erroMensagem.textContent = mensagem;
    erroDiv.classList.remove('d-none');
    
    setTimeout(() => {
        erroDiv.classList.add('d-none');
    }, 5000);
}

// Verificar se já está logado ao carregar a página de login
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    if (token) {
        // Já está logado, redirecionar
        window.location.href = 'index.html';
    }
});

// Permitir submit com Enter
document.getElementById('form-login')?.addEventListener('submit', (e) => {
    e.preventDefault();
    fazerLogin();
});
