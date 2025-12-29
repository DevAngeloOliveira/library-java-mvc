// Configuracao da API
const API_URL = 'http://localhost:8080';

// Estado do usuário
let usuarioLogado = null;

// Verificar autenticação ao carregar
document.addEventListener('DOMContentLoaded', () => {
    verificarAutenticacao();
    carregarTodos();
    carregarDisponiveis();
    carregarEmprestados();
});

// Verificar se usuário está autenticado
function verificarAutenticacao() {
    const usuarioData = localStorage.getItem('usuario');
    const navUserInfo = document.getElementById('nav-user-info');
    
    if (usuarioData) {
        usuarioLogado = JSON.parse(usuarioData);
        
        // Mostrar informações do usuário
        const tipoBadge = getTipoBadgeClass(usuarioLogado.tipo);
        navUserInfo.innerHTML = `
            <span class="nav-link">
                <span class="badge ${tipoBadge} me-2">
                    ${getTipoIcon(usuarioLogado.tipo)} ${getTipoNome(usuarioLogado.tipo)}
                </span>
                ${usuarioLogado.nome}
            </span>
        `;
        
        // Exibir painel de permissões
        exibirPermissoes();
    } else {
        // Modo sem autenticação
        navUserInfo.innerHTML = `
            <a class="nav-link text-warning" href="login.html">
                <i class="bi bi-exclamation-triangle me-1"></i>
                Fazer Login
            </a>
        `;
    }
}

// Exibir permissões do usuário
function exibirPermissoes() {
    const panel = document.getElementById('permissoes-panel');
    const lista = document.getElementById('permissoes-lista');
    
    if (!usuarioLogado) return;
    
    let permissoes = [];
    
    if (usuarioLogado.tipo === 'ADMIN') {
        permissoes = [
            '<span class="badge bg-danger me-1">✓ Todas as permissões</span>'
        ];
    } else if (usuarioLogado.tipo === 'BIBLIOTECARIO') {
        permissoes = [
            '<span class="badge bg-primary me-1">✓ Gerenciar Itens</span>',
            '<span class="badge bg-primary me-1">✓ Emprestar/Devolver Qualquer Item</span>',
            '<span class="badge bg-primary me-1">✓ Ver Estatísticas</span>'
        ];
    } else if (usuarioLogado.tipo === 'USUARIO') {
        permissoes = [
            '<span class="badge bg-success me-1">✓ Visualizar Acervo</span>',
            '<span class="badge bg-success me-1">✓ Emprestar/Devolver Próprios Itens</span>',
            '<span class="badge bg-warning text-dark me-1">⚠ Limite: 3 empréstimos</span>'
        ];
    }
    
    lista.innerHTML = permissoes.join('');
    panel.style.display = 'block';
}

// Obter nome amigável do tipo
function getTipoNome(tipo) {
    const nomes = {
        'ADMIN': 'Admin',
        'BIBLIOTECARIO': 'Bibliotecário',
        'USUARIO': 'Usuário'
    };
    return nomes[tipo] || tipo;
}

// Verificar permissão do usuário
function temPermissao(permissao) {
    if (!usuarioLogado) return false;
    
    // Admin tem todas as permissões
    if (usuarioLogado.tipo === 'ADMIN') return true;
    
    // Bibliotecário
    if (usuarioLogado.tipo === 'BIBLIOTECARIO') {
        const permissoesBibliotecario = [
            'CRIAR_ITEM', 'EDITAR_ITEM', 'EXCLUIR_ITEM', 'LISTAR_ITENS',
            'EMPRESTAR_QUALQUER_ITEM', 'DEVOLVER_QUALQUER_ITEM',
            'LISTAR_TODOS_EMPRESTIMOS', 'VISUALIZAR_ESTATISTICAS', 'LISTAR_USUARIOS'
        ];
        return permissoesBibliotecario.includes(permissao);
    }
    
    // Usuário Comum
    if (usuarioLogado.tipo === 'USUARIO') {
        const permissoesUsuario = [
            'LISTAR_ITENS', 'EMPRESTAR_PROPRIO_ITEM',
            'DEVOLVER_PROPRIO_ITEM', 'LISTAR_PROPRIOS_EMPRESTIMOS'
        ];
        return permissoesUsuario.includes(permissao);
    }
    
    return false;
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
    
    container.innerHTML = itens.map(item => {
        // Verificar permissões para ações
        const podeEmprestar = !item.emprestado && (
            temPermissao('EMPRESTAR_QUALQUER_ITEM') || 
            temPermissao('EMPRESTAR_PROPRIO_ITEM')
        );
        
        const podeDevolver = item.emprestado && (
            temPermissao('DEVOLVER_QUALQUER_ITEM') || 
            temPermissao('DEVOLVER_PROPRIO_ITEM')
        );
        
        const podeExcluir = temPermissao('EXCLUIR_ITEM');
        
        return `
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
                            <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                                <span class="badge ${item.emprestado ? 'bg-warning text-dark' : 'bg-success'}">
                                    ${item.emprestado ? 
                                        '<i class="bi bi-bookmark-fill me-1"></i>EMPRESTADO' : 
                                        '<i class="bi bi-check-circle-fill me-1"></i>DISPONÍVEL'}
                                </span>
                                <div class="btn-group" role="group">
                                    ${podeEmprestar ? 
                                        `<button class="btn btn-sm btn-primary" onclick="emprestar('${item.codigo}')" title="Emprestar item">
                                            <i class="bi bi-box-arrow-up-right me-1"></i>Emprestar
                                        </button>` : ''}
                                    ${podeDevolver ? 
                                        `<button class="btn btn-sm btn-success" onclick="devolver('${item.codigo}')" title="Devolver item">
                                            <i class="bi bi-box-arrow-in-down-left me-1"></i>Devolver
                                        </button>` : ''}
                                    ${podeExcluir ? 
                                        `<button class="btn btn-sm btn-danger" onclick="removerItem('${item.codigo}')" title="Excluir item">
                                            <i class="bi bi-trash me-1"></i>Excluir
                                        </button>` : ''}
                                </div>
                            </div>
                        </div>
                    ` : ''}
                </div>
            </div>
        `;
    }).join('');
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
    if (!temPermissao('EMPRESTAR_QUALQUER_ITEM') && !temPermissao('EMPRESTAR_PROPRIO_ITEM')) {
        mostrarToast('Você não tem permissão para emprestar itens', 'warning');
        return;
    }

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
    if (!temPermissao('DEVOLVER_QUALQUER_ITEM') && !temPermissao('DEVOLVER_PROPRIO_ITEM')) {
        mostrarToast('Você não tem permissão para devolver itens', 'warning');
        return;
    }

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

// Remover item
async function removerItem(codigo) {
    if (!temPermissao('EXCLUIR_ITEM')) {
        mostrarToast('Você não tem permissão para excluir itens', 'warning');
        return;
    }

    if (!confirm(`Tem certeza que deseja excluir o item ${codigo}?`)) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/api/item/remover`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: codigo })
        });
        
        const result = await response.json();
        
        if (result.sucesso) {
            mostrarToast(result.mensagem || 'Item removido com sucesso!', 'success');
            await recarregarTudo();
        } else {
            mostrarToast(result.mensagem || 'Erro ao remover item', 'danger');
        }
    } catch (error) {
        mostrarToast('Erro ao remover item', 'danger');
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
