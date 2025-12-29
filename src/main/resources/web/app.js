// Configuracao da API
const API_URL = 'http://localhost:8080';

// Verificar autenticação ao carregar
document.addEventListener('DOMContentLoaded', () => {
    verificarAutenticacao();
    carregarTodos();
    carregarDisponiveis();
    carregarEmprestados();
});

// Verificar se usuário está autenticado
function verificarAutenticacao() {
    const usuario = JSON.parse(localStorage.getItem('usuario') || 'null');
    const navUserInfo = document.getElementById('nav-user-info');
    
    if (usuario) {
        // Mostrar informações do usuário
        const tipoBadge = getTipoBadgeClass(usuario.tipo);
        navUserInfo.innerHTML = `
            <span class="nav-link">
                <span class="badge ${tipoBadge} me-2">
                    ${getTipoIcon(usuario.tipo)} ${usuario.tipo}
                </span>
                ${usuario.nome}
            </span>
        `;
    } else {
        // Modo sem autenticação
        navUserInfo.innerHTML = `
            <span class="nav-link text-warning">
                <i class="bi bi-exclamation-triangle me-1"></i>
                Modo sem autenticação
            </span>
        `;
    }
}

// Obter classe de badge por tipo de usuário
function getTipoBadgeClass(tipo) {
    const badges = {
        'ADMIN': 'bg-danger',
        'BIBLIOTECARIO': 'bg-primary',
        'USUARIO': 'bg-success'
    };
    return badges[tipo] || 'bg-secondary';
}

// Obter ícone por tipo de usuário
function getTipoIcon(tipo) {
    const icons = {
        'ADMIN': '<i class="bi bi-shield-fill-check"></i>',
        'BIBLIOTECARIO': '<i class="bi bi-person-badge"></i>',
        'USUARIO': '<i class="bi bi-person"></i>'
    };
    return icons[tipo] || '<i class="bi bi-person"></i>';
}

// Fazer logout
function fazerLogout() {
    const token = localStorage.getItem('token');
    
    if (token) {
        // Fazer logout no servidor
        fetch(`${API_URL}/api/auth/logout`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        }).catch(err => console.error('Erro ao fazer logout:', err));
    }
    
    // Limpar localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    
    // Redirecionar para login
    window.location.href = 'login.html';
}

// Carregar todos os itens
async function carregarTodos() {
    try {
        const response = await fetch(`${API_URL}/api/itens`);
        const apiResponse = await response.json();
        const itens = apiResponse.dados || [];
        renderizarItens(itens, 'lista-todos', true);
    } catch (error) {
        console.error('Erro ao carregar itens:', error);
        mostrarErroCarregamento('lista-todos');
    }
}

// Carregar itens disponiveis
async function carregarDisponiveis() {
    try {
        const response = await fetch(`${API_URL}/api/itens`);
        const apiResponse = await response.json();
        const itens = (apiResponse.dados || []).filter(item => !item.emprestado);
        renderizarItens(itens, 'lista-disponiveis', false);
        document.getElementById('count-disponiveis').textContent = `${itens.length} itens`;
    } catch (error) {
        console.error('Erro ao carregar disponiveis:', error);
        mostrarErroCarregamento('lista-disponiveis');
    }
}

// Carregar itens emprestados
async function carregarEmprestados() {
    try {
        const response = await fetch(`${API_URL}/api/itens`);
        const apiResponse = await response.json();
        const itens = (apiResponse.dados || []).filter(item => item.emprestado);
        renderizarItens(itens, 'lista-emprestados', false);
        document.getElementById('count-emprestados').textContent = `${itens.length} itens`;
    } catch (error) {
        console.error('Erro ao carregar emprestados:', error);
        mostrarErroCarregamento('lista-emprestados');
    }
}

// Renderizar itens na tela com Bootstrap
function renderizarItens(itens, containerId, mostrarAcoes) {
    const container = document.getElementById(containerId);
    
    if (itens.length === 0) {
        container.innerHTML = `
            <div class="col-12">
                <div class="alert alert-info">
                    <i class="bi bi-info-circle me-2"></i>
                    Nenhum item encontrado
                </div>
            </div>
        `;
        return;
    }
    
    container.innerHTML = itens.map(item => `
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 shadow-sm hover-card">
                <div class="card-header ${getHeaderClass(item.tipo)}">
                    <span class="badge bg-white text-dark">
                        ${getTipoIconCard(item.tipo)} ${item.tipo}
                    </span>
                </div>
                <div class="card-body">
                    <h5 class="card-title">${item.titulo}</h5>
                    <p class="card-text text-muted">
                        <small><i class="bi bi-hash me-1"></i>${item.codigo}</small>
                    </p>
                    <p class="card-text">${item.detalhes}</p>
                </div>
                ${mostrarAcoes ? `
                    <div class="card-footer bg-transparent border-top-0">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="badge ${item.emprestado ? 'bg-warning text-dark' : 'bg-success'}">
                                ${item.emprestado ? 
                                    '<i class="bi bi-bookmark-fill me-1"></i>EMPRESTADO' : 
                                    '<i class="bi bi-check-circle-fill me-1"></i>DISPONÍVEL'}
                            </span>
                            ${!item.emprestado ? 
                                `<button class="btn btn-sm btn-primary" onclick="emprestar('${item.codigo}')">
                                    <i class="bi bi-box-arrow-up-right me-1"></i>Emprestar
                                </button>` :
                                `<button class="btn btn-sm btn-success" onclick="devolver('${item.codigo}')">
                                    <i class="bi bi-box-arrow-in-down-left me-1"></i>Devolver
                                </button>`
                            }
                        </div>
                    </div>
                ` : ''}
            </div>
        </div>
    `).join('');
}

// Obter classe do header do card por tipo
function getHeaderClass(tipo) {
    const classes = {
        'LIVRO': 'bg-primary text-white',
        'REVISTA': 'bg-info text-white',
        'DVD': 'bg-success text-white'
    };
    return classes[tipo] || 'bg-secondary text-white';
}

// Obter ícone do card por tipo
function getTipoIconCard(tipo) {
    const icons = {
        'LIVRO': '<i class="bi bi-book"></i>',
        'REVISTA': '<i class="bi bi-journal-text"></i>',
        'DVD': '<i class="bi bi-disc"></i>'
    };
    return icons[tipo] || '<i class="bi bi-box"></i>';
}

// Mostrar erro de carregamento
function mostrarErroCarregamento(containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = `
        <div class="col-12">
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                Erro ao carregar dados. Verifique se o servidor está rodando.
            </div>
        </div>
    `;
}

// Emprestar item
async function emprestar(codigo) {
    try {
        const response = await fetch(`${API_URL}/api/item/emprestar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: codigo })
        });
        
        const result = await response.json();
        
        if (result.sucesso) {
            mostrarToast(result.mensagem || 'Item emprestado com sucesso!', 'success');
            await recarregarTudo();
        } else {
            mostrarToast(result.mensagem || 'Erro ao emprestar item', 'danger');
        }
    } catch (error) {
        mostrarToast('Erro ao emprestar item', 'danger');
    }
}

// Devolver item
async function devolver(codigo) {
    try {
        const response = await fetch(`${API_URL}/api/item/devolver`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: codigo })
        });
        
        const result = await response.json();
        
        if (result.sucesso) {
            mostrarToast(result.mensagem || 'Item devolvido com sucesso!', 'success');
            await recarregarTudo();
        } else {
            mostrarToast(result.mensagem || 'Erro ao devolver item', 'danger');
        }
    } catch (error) {
        mostrarToast('Erro ao devolver item', 'danger');
    }
}

// Recarregar todos os dados
async function recarregarTudo() {
    await carregarTodos();
    await carregarDisponiveis();
    await carregarEmprestados();
}

// Mostrar toast de notificação
function mostrarToast(mensagem, tipo) {
    const toastElement = document.getElementById('toast-notificacao');
    const toastMensagem = document.getElementById('toast-mensagem');
    const toastHeader = toastElement.querySelector('.toast-header');
    
    // Definir cor baseado no tipo
    toastHeader.className = `toast-header bg-${tipo === 'success' ? 'success' : 'danger'} text-white`;
    toastMensagem.textContent = mensagem;
    
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
}
