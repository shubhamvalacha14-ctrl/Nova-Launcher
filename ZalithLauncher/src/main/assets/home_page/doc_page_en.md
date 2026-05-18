# Welcome to Nova Launcher

...card-start title="Quick Actions" shape=extraLarge contentPadding=(16, 12)
Select your favorite version on the right panel and hit launch to start playing!
    
    ...row-start horizontal=spacedBy(8)
        ...button text="Manage Control Layouts" event="url {https://github.com/}" weight=(1)
        ...button-outlined text="Open Main Directory" event="url {https://github.com/}" weight=(1)
    ...row-end
...card-end

...card-start title="Tools" shape=large
    ...column-start vertical=spacedBy(8)
        ...button-filled-tonal text="Execute .jar File" event="url {https://github.com/}" width=100%
        ...button-text text="Share Log Files" event="share_game_log" width=100%
    ...column-end
...card-end
