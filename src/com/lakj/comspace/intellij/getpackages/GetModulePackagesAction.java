package com.lakj.comspace.intellij.getpackages;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This action class provides the action to show all the packages in the selected module.
 * Created by Lak J Comspace on 3/5/2015.
 */
public class GetModulePackagesAction extends AnAction {
    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        final Project project = DataKeys.PROJECT.getData(dataContext);
        final Module module = DataKeys.MODULE.getData(dataContext);

        final Set<String> packageNameSet = new HashSet<String>();

        AnalysisScope moduleScope = new AnalysisScope(module);
        moduleScope.accept(new PsiRecursiveElementVisitor() {
            @Override
            public void visitFile(final PsiFile file) {
                if (file instanceof PsiJavaFile) {
                    PsiJavaFile psiJavaFile = (PsiJavaFile) file;
                    final PsiPackage aPackage =
                            JavaPsiFacade.getInstance(project).findPackage(psiJavaFile.getPackageName());
                    if (aPackage != null) {
                        packageNameSet.add(aPackage.getQualifiedName());
                    }
                }
            }
        });

        String allPackageNames = "";
        for (String packageName : packageNameSet) {
            allPackageNames = allPackageNames + packageName + "\n";
        }

        Messages.showMessageDialog(project, allPackageNames,
                "All packages in selected module", Messages.getInformationIcon());
    }
}
